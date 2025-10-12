package ru.example.ToDoListSpring.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import ru.example.ToDoListSpring.model.enums.Status;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {
	private Long id;
	private String title;
	private String content;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	private String status; // optional: TODO, IN_PROGRESS, DONE
}
