package com.todo.todoapp.records;

import com.todo.todoapp.auth.Role;

import java.util.List;

public record UserViewRecord(Integer id, String username, String email, String password, Role role, String task, List<String> authorities ) {
}
