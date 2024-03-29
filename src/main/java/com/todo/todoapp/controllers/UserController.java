package com.todo.todoapp.controllers;



import com.todo.todoapp.records.AllUserInformationRecord;
import com.todo.todoapp.records.ChangePasswordReq;
import com.todo.todoapp.services.LogoutService;

import com.todo.todoapp.records.UserViewRecord;

import com.todo.todoapp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@RequiredArgsConstructor

public class UserController {
    private final UserService service;
    private final LogoutService logoutService;

    @PatchMapping ("/changepassword")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordReq request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/currentuser")
    public ResponseEntity<UserViewRecord> CurrentUser(Principal connectedUser) {
        UserViewRecord currentUser = service.findConnectedUser(connectedUser);
        System.out.println("Här kommer en användares förnamn:" + currentUser.username());
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
        return ResponseEntity.ok("Logged out successfully");
    }


    @PostMapping("/deactivateaccountwithlogout")
    public ResponseEntity<String> deactivateAccountWithLogOut(Principal connectedUser, HttpServletRequest request, HttpServletResponse response) {
        service.deactivateAccountWithLogOut(connectedUser, request, response);
        return ResponseEntity.ok("Deactivateaccount successfully");
    }


}
