package ru.example.ToDoListSpring.model;

import jakarta.persistence.*;
import lombok.*;
import ru.example.ToDoListSpring.model.enums.Status;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column
	private LocalDate date;

	@Column(nullable = false)
	private Status status;
}
