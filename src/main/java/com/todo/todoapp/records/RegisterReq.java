package com.todo.todoapp.records;

import com.todo.todoapp.auth.Role;

public record RegisterReq(
        String username,
        String email,
        String password,
        Role role) {
}
