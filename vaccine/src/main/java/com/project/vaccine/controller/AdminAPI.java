package com.project.vaccine.controller;


import com.project.vaccine.entity.User;
import com.project.vaccine.service.UserService;
import com.project.vaccine.service.VaccineService;
import com.project.vaccine.service.VerificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin")
@SecurityRequirement(name = "api")
public class AdminAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private VaccineService vaccineService;

    @GetMapping("/user")
    public ResponseEntity<?> getAllUserProfiles() {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
    }


    @GetMapping("/user/search")
    public void searchUserByName() {

    }


    @PostMapping("/user/delete/{id}")
    public void deleteUser() {

    }

    @PutMapping("/user/update/{id}")
    public void updateUser() {

    }

    @PostMapping("/user")
    public void createStaff() {

    }

    @GetMapping("/vaccine")
    public ResponseEntity<?> getAllVaccine() {
        return ResponseEntity.ok(vaccineService.getAllVaccines());
    }


}
