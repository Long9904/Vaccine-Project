package com.project.vaccine.controller;

import com.project.vaccine.dto.ChildDTO;
import com.project.vaccine.service.ChildService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/children")
@SecurityRequirement(name = "api")
@PreAuthorize("hasRole('USER')")
public class ChildAPI {

    @Autowired
    private ChildService childService;

    // Role user


    @PostMapping("/my")
    public ResponseEntity<?> createChildByCurrentUser(@Valid @RequestBody ChildDTO childDTO) {
        ChildDTO newChildDTO = childService.createChildByCurrentUser(childDTO);
        return ResponseEntity.ok(newChildDTO);
    }


    @GetMapping("/my")
    public ResponseEntity<?> getAllChildrenByCurrentUser() {
        // show all children for the current user
        return ResponseEntity.ok(childService.getChildrenByCurrentUser());
    }


    @PutMapping("/my/{id}")
    public ResponseEntity<?> updateChildByCurrentUser(@PathVariable Long id, @Valid @RequestBody ChildDTO childDTO) {
        ChildDTO updatedChildDTO = childService.updateChildByCurrentUser(id ,childDTO);
        return ResponseEntity.ok(updatedChildDTO);
    }


    @DeleteMapping("/my/{id}")
    public ResponseEntity<?> deleteChildByCurrentUser(@PathVariable Long id) {
        childService.deleteChildByCurrentUser(id);
        return ResponseEntity.ok("Child deleted successfully");
    }

}


