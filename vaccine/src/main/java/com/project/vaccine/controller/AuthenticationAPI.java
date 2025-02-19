package com.project.vaccine.controller;


import com.project.vaccine.dto.request.RegisterRequest;
import com.project.vaccine.dto.request.LoginRequest;
import com.project.vaccine.dto.response.LoginResponse;
import com.project.vaccine.exception.DuplicateException;
import com.project.vaccine.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("api/authentication")
public class AuthenticationAPI {

    //DI: Dependency Injection
    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity <?> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authenticationService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            String notify = authenticationService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", notify));
        } catch (DuplicateException e){
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Logout successful"));
    }


    @GetMapping
    public ResponseEntity<?> testLogin(@RequestParam String token) {
        if(authenticationService.testLogin(token)) {
            return ResponseEntity.ok(Map.of("message", "Login successful"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Login failed"));
        }
    }

}
