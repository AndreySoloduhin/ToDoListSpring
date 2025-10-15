package ru.example.ToDoListSpring.dto;

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
public class TaskResponse {
	private Long id;
	private String title;
	private String content;
	private LocalDate date;
	private Status status;
}
