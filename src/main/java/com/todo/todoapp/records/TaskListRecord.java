package com.todo.todoapp.records;

import java.time.LocalDate;
import java.time.LocalTime;


public record TaskListRecord(Integer id, String task, LocalDate dateoftask, LocalTime timeoftask, boolean isPerformed) {
}
