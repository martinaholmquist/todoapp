package com.todo.todoapp.services;

import com.todo.todoapp.auth.ChangePasswordRequest;
import com.todo.todoapp.auth.Permission;
import com.todo.todoapp.auth.Role;
import com.todo.todoapp.config.LogoutService;
import com.todo.todoapp.models.Todo;
import com.todo.todoapp.models.User;
import com.todo.todoapp.records.AllUserInformationRecord;
import com.todo.todoapp.records.UserViewRecord;
import com.todo.todoapp.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final LogoutService logoutService;


    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
    }


    public UserViewRecord findConnectedUser(Principal connectedUser) {
        var ofConnectedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        return repository.findByEmail(ofConnectedUser.getEmail())
                .map(user -> {
                    Todo userTodo = user.getTodo();
                    String task = (userTodo != null) ? userTodo.getTask() : null;

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
                            task,
                            authorities
                    );
                })
                .orElseThrow(() -> new RuntimeException("User not found in my method...."));
    }


    public List<AllUserInformationRecord> allUserInformationRecord() {
        List<User> users = repository.findAll();

        if (users.isEmpty()) {
            // If no users, return empty list
            return Collections.emptyList();
        }
        List<AllUserInformationRecord> userRecords = users.stream()
                .map(user -> {
                    Todo todo = user.getTodo();
                    String task = todo != null ? todo.getTask() : null;


                    return new AllUserInformationRecord(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            task
                    );
                })
                .collect(Collectors.toList());

        return userRecords;
    }




    public void deactivateAccount(Principal connectedUser, HttpServletRequest request,
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

