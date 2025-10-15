package ru.example.ToDoListSpring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.example.ToDoListSpring.dto.TaskRequest;
import ru.example.ToDoListSpring.dto.TaskResponse;
import ru.example.ToDoListSpring.model.enums.Status;
import ru.example.ToDoListSpring.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

	private final TaskService taskRepository;

	@GetMapping
	public List<TaskResponse> findAllTasks() {
		return taskRepository.findAllTasks();
	};

	@GetMapping("find/{id}")
	public TaskResponse findTaskById(@PathVariable Long id) {
		return taskRepository.findTaskById(id);
	}

	@GetMapping("filter_by_status/{status}")
	public List<TaskResponse> filterByStatus(@PathVariable String status) {
		Status enumStatus = Status.fromString(status);
		return taskRepository.filterTasksByStatus(enumStatus);
	}

	@GetMapping("sort")
	public List<TaskResponse> sortTasks(@RequestParam(defaultValue = "deadline") String sortBy,
										@RequestParam(defaultValue = "asc") String order) {
		return taskRepository.sortTasks(sortBy, order);
	}

	@PostMapping("create")
	public TaskResponse createTask(@Valid @RequestBody TaskRequest request) {
		return taskRepository.saveTask(request);
	}

	@PutMapping("update")
	public TaskResponse updateTask(@Valid @RequestBody TaskRequest request) {
		return taskRepository.updateTask(request);
	}

	@DeleteMapping("delete/{id}")
	public String deleteTaskById(@PathVariable Long id) {
		taskRepository.deleteTask(id);
		return "Объект успешно удален";
	}
}
