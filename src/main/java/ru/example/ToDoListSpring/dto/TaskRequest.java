package ru.example.ToDoListSpring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.example.ToDoListSpring.model.enums.Status;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {

	private Long id;

	@NotBlank(message = "Название задачи не может быть пустым")
	@Size(max = 100, message = "Название не должно быть длиннее 100 символов")
	private String title;

	@Size(max = 300, message = "Содержание не должно быть длиннее 500 символов")
	private String content;

	@NotNull(message = "Дата задачи не может быть null")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@NotNull(message = "Статус задачи не может быть null")
	private Status status;
}
