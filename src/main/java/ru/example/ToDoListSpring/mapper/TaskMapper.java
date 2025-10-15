package ru.example.ToDoListSpring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.example.ToDoListSpring.dto.TaskRequest;
import ru.example.ToDoListSpring.dto.TaskResponse;
import ru.example.ToDoListSpring.model.Task;
import ru.example.ToDoListSpring.model.enums.Status;

@Mapper(componentModel = "spring")
public interface TaskMapper {

	@Mapping(target = "id", ignore = true)
	Task toEntity(TaskRequest request);

	TaskResponse toResponse(Task task);

	default Status mapStatus(Status status) {
		if (status == null) return Status.TODO;
		try {
			return Status.valueOf(status.name());
		} catch (IllegalArgumentException e) {
			return Status.TODO;
		}
	}

}
