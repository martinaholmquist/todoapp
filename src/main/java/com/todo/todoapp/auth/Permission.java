package com.todo.todoapp.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    USER_READ("user:read"),

    USER_CREATE("user:create"),

    USER_UPDATE("user:update"),
    ;


    @Getter
    private final String permission;
}
