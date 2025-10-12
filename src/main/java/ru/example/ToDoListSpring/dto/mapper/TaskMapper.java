package ru.example.ToDoListSpring.dto.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.example.ToDoListSpring.dto.TaskRequest;
import ru.example.ToDoListSpring.dto.TaskResponse;
import ru.example.ToDoListSpring.model.Task;
import ru.example.ToDoListSpring.model.enums.Status;

@Mapper(componentModel = "spring")
public interface TaskMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", expression = "java(mapStatus(request.getStatus()))")
	Task toEntity(TaskRequest request);

	@Mapping(target = "status", expression = "java(task.getStatus() != null ? task.getStatus().name() : null)")
	TaskResponse toResponse(Task task);

	@AfterMapping
	default void ensureDefaults(@MappingTarget Task task) {
		if (task.getStatus() == null) task.setStatus(Status.TODO);
	}

	default Status mapStatus(String status) {
		if (status == null) return Status.TODO;
		try {
			return Status.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			return Status.TODO;
		}
	}

}
