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

@RestController
@RequestMapping("api/admin")
public class AdminAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService verificationService;


    @GetMapping("/user")
    public ResponseEntity getAllUserProfiles() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Admin
    @GetMapping("/user/search/{username}")
    public void searchUserByUsername() {

    }

    @GetMapping("/user/search/{email}")
    public void searchUserByEmail() {

    }

    @GetMapping("/user/search/{phone}")
    public void searchUserByPhone() {

    }

    @GetMapping("/user/search/{address}")
    public void searchUserByAddress() {

    }

    @GetMapping("/user/search/{name}")
    public void searchUserByName() {

    }

    @GetMapping("/user/filter")
    public void fillterUser() {

    }

    // Admin
    // Set user status = 0 (inactive)
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
