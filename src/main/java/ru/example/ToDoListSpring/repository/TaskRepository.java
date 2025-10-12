package ru.example.ToDoListSpring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.example.ToDoListSpring.model.Task;
import ru.example.ToDoListSpring.model.enums.Status;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByStatus(Status status);
}
