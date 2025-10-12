package ru.example.ToDoListSpring.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.ToDoListSpring.dto.TaskRequest;
import ru.example.ToDoListSpring.dto.TaskResponse;
import ru.example.ToDoListSpring.dto.mapper.TaskMapper;
import ru.example.ToDoListSpring.model.Task;
import ru.example.ToDoListSpring.model.enums.Status;
import ru.example.ToDoListSpring.repository.TaskRepository;
import ru.example.ToDoListSpring.service.TaskService;
import ru.example.ToDoListSpring.service.exception.ResourceNotFoundException;
import ru.example.ToDoListSpring.service.exception.ValidationException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;
	private final TaskMapper mapper;

	@Override
	@Transactional
	public List<TaskResponse> findAllTasks() {
		return taskRepository.findAll()
				.stream()
				.map(mapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public TaskResponse findTaskById(Long id) {
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Task с id " + id + " не найден"));
		return mapper.toResponse(task);
	}

	@Override
	public TaskResponse saveTask(TaskRequest request) {
		validateRequest(request);

		Task task = mapper.toEntity(request);
		Task saved = taskRepository.save(task);

		return mapper.toResponse(saved);
	}

	@Override
	public TaskResponse updateTask(TaskRequest request) {
		if (request.getId() == null) {
			throw new ValidationException("Поле id обязательно для обновления");
		}

		validateRequest(request);

		Task existing = taskRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Task с id " + request.getId() + " не найден"));

		if (request.getTitle() != null) existing.setTitle(request.getTitle());
		if (request.getContent() != null) existing.setContent(request.getContent());
		if (request.getDate() != null) existing.setDate(request.getDate());
		if (request.getStatus() != null)
			existing.setStatus(mapper.mapStatus(String.valueOf(request.getStatus())));

		Task updated = taskRepository.save(existing);
		return mapper.toResponse(updated);
	}

	@Override
	public List<TaskResponse> filterTasksByStatus(Status status) {
		return taskRepository.findByStatus(status)
				.stream()
				.map(mapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteTask(Long id) {
		if (!taskRepository.existsById(id)) {
			throw new ResourceNotFoundException("Task с id " + id + " не найден");
		}
		taskRepository.deleteById(id);
	}

	@Override
	public List<TaskResponse> sortTasks(String sortBy, String order) {
		boolean asc = switch (order.toLowerCase()) {
			case "asc" -> true;
			case "desc" -> false;
			default -> throw new IllegalArgumentException("Неверный параметр order: " + order);
		};

		Map<Status, Integer> statusOrder = Map.of(
				Status.TODO, 1,
				Status.IN_PROGRESS, 2,
				Status.DONE, 3
		);

		Comparator<Task> comparator = switch (sortBy.toLowerCase()) {
			case "status" -> Comparator.comparing(
					t -> statusOrder.getOrDefault(t.getStatus(), Integer.MAX_VALUE)
			);
			case "deadline" -> Comparator.comparing(Task::getDate, Comparator.nullsLast(Comparator.naturalOrder()));
			default -> throw new IllegalArgumentException("Неверный параметр sortBy: " + sortBy);
		};

		if (!asc) comparator = comparator.reversed();

		return taskRepository.findAll()
				.stream()
				.sorted(comparator)
				.map(mapper::toResponse)
				.collect(Collectors.toList());
	}

	private void validateRequest(TaskRequest request) {
		if (request.getTitle() == null || request.getContent() == null || request.getStatus() == null) {
			throw new ValidationException("Поля title, description и status обязательны");
		}
	}
}
