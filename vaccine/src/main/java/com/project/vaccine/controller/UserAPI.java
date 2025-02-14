package com.project.vaccine.controller;

import com.project.vaccine.dto.response.UserResponse;
import com.project.vaccine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("api/user")
public class UserAPI {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@RequestParam String token) {
      try {
            UserResponse userResponse = userService.getUserFromToken(token);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
      }
    }
}
