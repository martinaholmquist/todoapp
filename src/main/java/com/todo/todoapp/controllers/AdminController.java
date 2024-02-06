package com.todo.todoapp.controllers;

import com.todo.todoapp.records.AllUserInformationRecord;
import com.todo.todoapp.records.DeleteUser;
import com.todo.todoapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;


//@CrossOrigin(origins = "http://localhost:3000/**", methods = {RequestMethod.GET, RequestMethod.DELETE}, allowedHeaders = "Authorization")
@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService service;




    @GetMapping("/alluserinfo")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<AllUserInformationRecord>> AllUserInformationRecord() {
        List<AllUserInformationRecord> userRecords = service.allUserInformationRecord();
        if (userRecords.isEmpty()) {
            System.out.println("Här kommer userRecords:" + userRecords);
            return new ResponseEntity<>(userRecords, HttpStatus.NO_CONTENT);

        } else {
            return new ResponseEntity<>(userRecords, HttpStatus.OK);
        }
    }


    //CrossOrigin(origins = "*", methods = { RequestMethod.DELETE }, allowedHeaders = "*")
    @DeleteMapping("/deleteuser/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable int id, Principal connectedUser) {
        service.deleteUser(id, connectedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

