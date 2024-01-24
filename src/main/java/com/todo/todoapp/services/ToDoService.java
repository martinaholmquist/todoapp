package com.todo.todoapp.services;


import com.todo.todoapp.models.Todo;
import com.todo.todoapp.models.User;
import com.todo.todoapp.repositories.ToDoRepository;
import com.todo.todoapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoRepository todoRepository;
    private final UserRepository userrepository;
    public List<String> getTasks() {
        try {
            List<String> tasksList = todoRepository.findAllTasks();

            return tasksList != null ? tasksList : Collections.emptyList();
        } catch (RuntimeException e) {
            log.error("error while fetching tasks: ", e);
            throw new RuntimeException("Error fetching tasks", e);
        }
    }



    public void addTask(Todo request, Principal connectedUser) {
        var ofConnectedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Optional<User> optionalUser = userrepository.findByEmail(ofConnectedUser.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Integer id = user.getId();
            String task = request.getTask();
            Todo taskFromUser = new Todo();
            taskFromUser.setUser(user);
            taskFromUser.setTask(task);


            todoRepository.save(taskFromUser);
            System.out.println("här skapas taskFromUser" + taskFromUser);

        } else {
            throw new RuntimeException("user not found i min metod....");
        }

    }


}
