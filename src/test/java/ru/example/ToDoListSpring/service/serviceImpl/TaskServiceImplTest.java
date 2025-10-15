package ru.example.ToDoListSpring.service.serviceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.example.ToDoListSpring.dto.TaskRequest;
import ru.example.ToDoListSpring.dto.TaskResponse;
import ru.example.ToDoListSpring.mapper.TaskMapper;
import ru.example.ToDoListSpring.model.Task;
import ru.example.ToDoListSpring.model.enums.Status;
import ru.example.ToDoListSpring.repository.TaskRepository;
import ru.example.ToDoListSpring.service.exception.ValidationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

	@Mock
	private TaskRepository TaskRepository;

	@Mock
	private TaskMapper mapper;

	@InjectMocks
	private TaskServiceImpl TaskService;

	private Task Task1;
	private Task Task2;

	@BeforeEach
	void setUp() {
		Task1 = new Task();
		Task1.setId(1L);
		Task1.setTitle("Task 1");
		Task1.setContent("Content 1");
		Task1.setStatus(Status.TODO);
		Task1.setDate(LocalDate.of(2025, 10, 10));

		Task2 = new Task();
		Task2.setId(2L);
		Task2.setTitle("Task 2");
		Task2.setContent("Content 2");
		Task2.setStatus(Status.DONE);
		Task2.setDate(LocalDate.of(2025, 10, 12));
	}

	@Test
	void testFindAllTasks_Success() {
		List<Task> Tasks = List.of(Task1, Task2);
		when(TaskRepository.findAll()).thenReturn(Tasks);

		List<TaskResponse> result = TaskService.findAllTasks();

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(TaskRepository).findAll();
	}

	@Test
	void testFindTasksById_Found() {
		when(TaskRepository.findById(1L)).thenReturn(Optional.of(Task1));

		TaskResponse result = TaskService.findTaskById(1L);

		assertNotNull(result);
		assertEquals(Task1, result);
		verify(TaskRepository).findById(1L);
	}

	@Test
	void testFindTaskById_NotFound() {
		when(TaskRepository.findById(3L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> TaskService.findTaskById(3L));
		verify(TaskRepository).findById(3L);
	}

//	@Test
//	void testSaveTask_Success() {
//		taskRequest = new TaskRequest();
//		taskRequest.setTitle("Task 1");
//		taskRequest.setContent("Content 1");
//		taskRequest.setStatus(Status.DONE);
//		taskRequest.setDate(LocalDate.of(2025, 10, 10));
//
//		when(mapper.toEntity(request)).thenReturn(entity);
//		when(repository.save(entity)).thenReturn(savedEntity);
//		when(mapper.toResponse(savedEntity)).thenReturn(response);
//		TaskResponse saved = TaskService.saveTask(taskRequest);
//
//		assertEquals(Task1.getTitle(), saved.getTitle());
//		verify(TaskRepository).save(Task1);
//	}

	@Test
	void testSaveTask_ValidationException() {
		TaskRequest invalidRequest = TaskRequest.builder()
				.content("Test description only")
				.build();

		assertThrows(ValidationException.class, () -> TaskService.saveTask(invalidRequest));

		verify(TaskRepository, never()).save(any());
	}

//	@Test
//	void testUpdateTask_Success() {
//		when(TaskRepository.save(Task1)).thenReturn(Task1);
//
//		Task updated = TaskService.updateTask(Task1);
//
//		assertEquals(Task1.getTitle(), updated.getTitle());
//		verify(TaskRepository).save(Task1);
//	}

	@Test
	void testFilterTasks_ByStatus() {
		when(TaskRepository.findByStatus(Status.TODO)).thenReturn(List.of(Task1));

		List<TaskResponse> filtered = TaskService.filterTasksByStatus(Status.TODO);

		assertEquals(1, filtered.size());
		assertEquals(Status.TODO, filtered.get(0).getStatus());
		verify(TaskRepository).findByStatus(Status.TODO);
	}

	@Test
	void testDeleteTask_Success() {
		when(TaskRepository.existsById(1L)).thenReturn(true);
		doNothing().when(TaskRepository).deleteById(1L);

		TaskService.deleteTask(1L);

		verify(TaskRepository).existsById(1L);
		verify(TaskRepository).deleteById(1L);
	}

	@Test
	void testDeleteTask_NotFound() {
		when(TaskRepository.existsById(3L)).thenReturn(false);

		assertThrows(RuntimeException.class, () -> TaskService.deleteTask(3L));
		verify(TaskRepository).existsById(3L);
		verify(TaskRepository, never()).deleteById(any());
	}

	@Test
	void testSortTasks_ByDeadlineDesc() {
		when(TaskRepository.findAll()).thenReturn(List.of(Task1, Task2));

		List<TaskResponse> sorted = TaskService.sortTasks("deadline", "desc");

		assertEquals(Task2, sorted.get(0));
		assertEquals(Task1, sorted.get(1));
	}

	@Test
	void testSortTasks_InvalidSortBy() {
		when(TaskRepository.findAll()).thenReturn(List.of(Task1, Task2));

		assertThrows(IllegalArgumentException.class, () -> TaskService.sortTasks("invalid", "asc"));
	}

	@Test
	void testSortTasks_InvalidOrder() {
		assertThrows(IllegalArgumentException.class, () -> TaskService.sortTasks("status", "wrong"));
	}
}
