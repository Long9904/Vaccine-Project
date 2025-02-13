package com.project.vaccine.controller;


import com.project.vaccine.entity.User;
import com.project.vaccine.service.UserService;
import com.project.vaccine.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin")
public class AdminAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService verificationService;


    @PostMapping("/user")
    public ResponseEntity<?> createUser() {
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User created successfully"));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllUserProfiles() {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
    }


    @GetMapping("/user/search/{name}")
    public void searchUserByName() {

    }


    @PostMapping("/user/delete/{id}")
    public void deleteUser() {

    }

    @PostMapping("/user/update/{id}")
    public void updateUser() {

    }

    @PostMapping("/staff")
    public void createStaff() {

    }


    @GetMapping("/staff")
    public void getAllStaff() {

    }




}
