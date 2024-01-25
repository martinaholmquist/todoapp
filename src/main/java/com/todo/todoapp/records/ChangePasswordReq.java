package com.todo.todoapp.records;

public record ChangePasswordReq(String currentPassword,
                                String newPassword,
                                String confirmationPassword) {
}
