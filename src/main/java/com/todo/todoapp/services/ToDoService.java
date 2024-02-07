package com.todo.todoapp.services;


import com.todo.todoapp.models.Todo;
import com.todo.todoapp.models.User;
import com.todo.todoapp.records.TaskListRecord;
import com.todo.todoapp.records.UpdatePerformed;
import com.todo.todoapp.repositories.ToDoRepository;
import com.todo.todoapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


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



    public List<TaskListRecord> getTasks(Principal connectedUser) throws IOException {
        try {
            var ofConnectedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

            List<Todo> tasksList = todoRepository.findByUserEmail(ofConnectedUser.getEmail());

        if (tasksList.isEmpty()) {
            return Collections.emptyList();
        }

        List<TaskListRecord> result = new ArrayList<>();

        for (Todo task : tasksList) {
            Integer id = task.getId();
            LocalTime hour = task.getTimeoftask();
            LocalDate date = task.getDateoftask();
            String taskOfUser = task.getTask();
            boolean performed = task.isPerformed();
            result.add(new TaskListRecord(id, taskOfUser, date, hour, performed));
        }
        return result;
        } catch (RuntimeException e) {
            log.error("Error while fetching tasks: ", e);
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
            LocalDate dateoftask= request.getDateoftask();
            LocalTime timeoftask= request.getTimeoftask();
            boolean performed= request.isPerformed();

            Todo taskFromUser = new Todo();
            taskFromUser.setUser(user);
            taskFromUser.setTask(task);
            taskFromUser.setDateoftask(dateoftask);
            taskFromUser.setTimeoftask(timeoftask);
            taskFromUser.setPerformed(performed);


            todoRepository.save(taskFromUser);
            System.out.println("här skapas taskFromUser" + taskFromUser);

        } else {
            throw new RuntimeException("user not found i min metod....");
        }
    }

    public void updateTaskPerformed(UpdatePerformed request, Principal connectedUser) {
        var ofConnectedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Optional<Todo> optionalToDo = todoRepository.findById(request.id());
        optionalToDo.ifPresent(todo -> {
            todo.setPerformed(request.isPerformed());
            todoRepository.save(todo);
        });
        if (optionalToDo.isEmpty()) {
            throw new RuntimeException("Todo not found in my method....");
        }
    }

    public void deleteTask(UpdatePerformed request, Principal connectedUser) {
        var ofConnectedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Optional<Todo> optionalToDo = todoRepository.findById(request.id());
        optionalToDo.ifPresent(todo -> {
            todoRepository.deleteById(request.id());
        });
        if (optionalToDo.isEmpty()) {
            throw new RuntimeException("Todo not found in my method....");
        }
    }
}
