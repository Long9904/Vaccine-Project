package com.project.vaccine.controller;

import com.project.vaccine.dto.ChildDTO;
import com.project.vaccine.service.ChildService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/children")
@SecurityRequirement(name = "api")
public class ChildAPI {

    @Autowired
    private ChildService childService;

    @PostMapping
    public ResponseEntity<?> createChild(@Valid @RequestBody ChildDTO childDTO) {
        ChildDTO newChildDTO = childService.createChild(childDTO);
        return ResponseEntity.ok(newChildDTO);
    }

    @GetMapping
    public ResponseEntity<?> getAllChildren() {
        // show all children for the current user
        return ResponseEntity.ok(childService.getChildren());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateChild(@PathVariable Long id, @Valid @RequestBody ChildDTO childDTO) {
        ChildDTO updatedChildDTO = childService.updateChild(id ,childDTO);
        return ResponseEntity.ok(updatedChildDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChild(@PathVariable Long id) {
        childService.deleteChild(id);
        return ResponseEntity.ok("Child deleted successfully");
    }
}


