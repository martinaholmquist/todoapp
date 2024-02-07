package com.todo.todoapp.services;


import com.todo.todoapp.models.Todo;
import com.todo.todoapp.models.User;
import com.todo.todoapp.records.*;
import com.todo.todoapp.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final LogoutService logoutService;


    public void changePassword(ChangePasswordReq request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        repository.save(user);
    }


    public UserViewRecord findConnectedUser(Principal connectedUser) {
        var ofConnectedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        return repository.findByEmail(ofConnectedUser.getEmail())
                .map(user -> {
                    List<Todo> userTodos = user.getTodos();

                    // Get tasks as a list of strings
                    List<String> tasks = userTodos.stream()
                            .map(Todo::getTask)
                            .collect(Collectors.toList());

                    // Get authorities from SecurityContextHolder
                    List<String> authorities = SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList());

                    return new UserViewRecord(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getRole(),
                            tasks,
                            authorities
                    );
                })
                .orElseThrow(() -> new RuntimeException("User not found in my method...."));
    }


    public List<AllUserInformationRecord> allUserInformationRecord() {
        List<User> users = repository.findAll();

        if (users.isEmpty()) {
            return Collections.emptyList();
        }
        List<AllUserInformationRecord> userRecords = users.stream()
                .map(user -> {

                    List<Todo> userTodos = user.getTodos();

                    // Get tasks as a list of strings
                    List<String> tasks = userTodos.stream()
                            .map(Todo::getTask)
                            .collect(Collectors.toList());



                    return new AllUserInformationRecord(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.isActive()
                    );
                })
                .collect(Collectors.toList());

        return userRecords;
    }




    public void deleteUser(int id, Principal connectedUser) {
            var ofConnectedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            repository.deleteById(id);
    }



    public void deactivateAccountWithLogOut(Principal connectedUser, HttpServletRequest request,
                                  HttpServletResponse response) {
        var ofConnectedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        User userToDeactivate = repository.findByEmail(ofConnectedUser.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found in my method...."));

        userToDeactivate.setActive(false);
        repository.save(userToDeactivate);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logoutService.logout(request, response, authentication);
    }








}

