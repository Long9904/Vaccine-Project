package com.project.vaccine.controller;

import com.project.vaccine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/user")
public class UserAPI {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> viewProfile(@RequestParam String token) {

    return ResponseEntity.ok("User profile");
    }
}
