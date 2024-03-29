package com.todo.todoapp.controllers;


import com.todo.todoapp.models.Todo;
import com.todo.todoapp.records.AllUserInformationRecord;
import com.todo.todoapp.records.TaskListRecord;
import com.todo.todoapp.records.UpdatePerformed;
import com.todo.todoapp.services.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@RequiredArgsConstructor
@CrossOrigin
public class TaskController {

    private final ToDoService service;

//används i postman
    @GetMapping("/alltasks")
    public ResponseEntity<List<String>> listAllTasks() throws IOException {
        List<String> tasks = service.getTasks();

            return ResponseEntity.ok(tasks);

    }




    @GetMapping("/mytasks")
    public ResponseEntity<List<TaskListRecord>> getTasks(Principal connectedUser) throws IOException {
        List<TaskListRecord> tasks = service.getTasks(connectedUser);

        if (tasks.isEmpty()) {
            System.out.println("Här kommer userRecords:" + tasks);
            return new ResponseEntity<>(tasks, HttpStatus.NO_CONTENT);
        } else {
            // Sortera listan i datumordning och placera de som inte är utförda först
            Collections.sort(tasks, Comparator
                    .comparing(TaskListRecord::isPerformed)
                    .thenComparing(TaskListRecord::dateoftask));

            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }
    }



    @PostMapping("/addtask")
    public ResponseEntity<Void> addTask(@RequestBody Todo request, Principal connectedUser) {
        service.addTask(request, connectedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/updatetaskperformed")
    public ResponseEntity<Void> updateTaskPerformed(@RequestBody UpdatePerformed request, Principal connectedUser) {
        service.updateTaskPerformed(request, connectedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/deletetask")
    public ResponseEntity<Void> deleteTask(@RequestBody UpdatePerformed request, Principal connectedUser) {
        service.deleteTask(request, connectedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
