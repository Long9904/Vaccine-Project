package com.project.vaccine.controller;


import com.project.vaccine.dto.ChildDTO;
import com.project.vaccine.dto.UserDTO;
import com.project.vaccine.entity.User;
import com.project.vaccine.service.ChildService;
import com.project.vaccine.service.UserService;
import com.project.vaccine.service.VaccineService;
import com.project.vaccine.service.VerificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin")
@SecurityRequirement(name = "api")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private VaccineService vaccineService;

    @Autowired
    private ChildService childService;


    // User API for Admin
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
    }


    // Child API for Admin
    @GetMapping("/children/{name}")
    public ResponseEntity<?> getChildByName(@PathVariable String name) {
        return ResponseEntity.ok(childService.getChildByName(name));
    }

    @GetMapping("/children")
    public ResponseEntity<?> getAllChildren() {
        return ResponseEntity.ok(childService.getAllChildren());
    }

    @PutMapping("/children/{id}")
    public ResponseEntity<?> updateChild(@PathVariable Long id, @Valid @RequestBody ChildDTO childDTO) {
        ChildDTO updatedChildDTO = childService.updateChildById(id, childDTO);
        return ResponseEntity.ok(updatedChildDTO);
    }

    @DeleteMapping("/children/{id}")
    public ResponseEntity<?> deleteChild(@PathVariable Long id) {
        if(childService.deleteChildById(id)) {
            return ResponseEntity.ok("Child deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Child not found");
        }
    }

}
