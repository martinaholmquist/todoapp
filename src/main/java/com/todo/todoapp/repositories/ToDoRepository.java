package com.todo.todoapp.repositories;

import com.todo.todoapp.models.Todo;
import com.todo.todoapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<Todo, Integer> {

    @Query("SELECT f.task FROM Todo f")
    List<String> findAllTasks();

    void deleteByUser(User user);

    List<Todo> findByUserEmail(String email);

    //List<Todo> findAllTasks();

}
