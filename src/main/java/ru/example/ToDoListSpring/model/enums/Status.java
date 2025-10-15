package ru.example.ToDoListSpring.model.enums;

public enum Status {
	TODO,
	IN_PROGRESS,
	DONE;

	public static Status fromString(String value) {
		if (value == null) return null;
		return Status.valueOf(value.toUpperCase());
	}
}
