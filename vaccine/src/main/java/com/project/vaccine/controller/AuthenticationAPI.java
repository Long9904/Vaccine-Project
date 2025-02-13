package com.project.vaccine.controller;


import com.project.vaccine.dto.LoginDTO;
import com.project.vaccine.dto.UserDTO;
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


    // Admin, Staff, User
    @PostMapping("/login")
    public ResponseEntity <?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            UserDTO userDTO = authenticationService.login(loginDTO.getUsername(), loginDTO.getPassword());
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    // Admin, Staff, User
    @PostMapping("/logout")
    public ResponseEntity <?> logout(@RequestParam String username) {
        return ResponseEntity.ok(Map.of("message", "Logout successfully"));
    }


    // Admin, Staff, User
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO) {
        try {
            String notify = authenticationService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", notify));
        } catch (DuplicateException e){
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "Đã có lỗi xảy ra. Vui lòng thử lại sau!"
            ));
        }
    }


}
