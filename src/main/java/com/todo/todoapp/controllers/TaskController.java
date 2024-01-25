package com.todo.todoapp.controllers;


import com.todo.todoapp.models.Todo;
import com.todo.todoapp.services.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin
public class TaskController {

    private final ToDoService service;




    @GetMapping("/alltasks")
    public ResponseEntity<List<String>> listAllTasks() throws IOException {
        List<String> tasks = service.getTasks();

            return ResponseEntity.ok(tasks);

    }



    @PostMapping("/addtask")
    public ResponseEntity<Void> addTask(@RequestBody Todo request, Principal connectedUser) {
        service.addTask(request, connectedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
