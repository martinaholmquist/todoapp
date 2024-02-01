package com.todo.todoapp.records;

import java.util.List;

public record AllUserInformationRecord(Integer id, String username, String email, List<String> tasks) {
}

