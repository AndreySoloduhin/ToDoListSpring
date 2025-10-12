package ru.example.ToDoListSpring.service;

import ru.example.ToDoListSpring.dto.TaskRequest;
import ru.example.ToDoListSpring.dto.TaskResponse;
import ru.example.ToDoListSpring.model.enums.Status;

import java.util.List;

public interface TaskService {

	List<TaskResponse> findAllTasks();
	TaskResponse findTaskById(Long id);
	TaskResponse saveTask(TaskRequest request);
	TaskResponse updateTask(TaskRequest request);
	List<TaskResponse> filterTasksByStatus(Status status);
	List<TaskResponse> sortTasks(String sortBy, String order);
	void deleteTask(Long id);


}
