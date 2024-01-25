package com.todo.todoapp.controllers;

import com.todo.todoapp.records.AllUserInformationRecord;
import com.todo.todoapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@CrossOrigin  //la till detta efter problem med cors i frontend.....
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService service;


    @GetMapping("/alluserinfo")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<AllUserInformationRecord>> AllUserInformationRecordOne() {
        List<AllUserInformationRecord> userRecords = service.allUserInformationRecord();
        if (userRecords.isEmpty()) {
            System.out.println("Här kommer userRecords:" + userRecords);
            return new ResponseEntity<>(userRecords, HttpStatus.NO_CONTENT);

        } else {
            return new ResponseEntity<>(userRecords, HttpStatus.OK);
        }
    }


    @DeleteMapping ("/deleteusermanually")
    //@PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> deleteuser(
            Principal connectedUser
    ) {
        service.deleteusermanually(connectedUser);
        return ResponseEntity.ok("Deleted");
    }


}

