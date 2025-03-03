package com.project.vaccine.controller;

import com.project.vaccine.dto.ChildDTO;
import com.project.vaccine.service.ChildService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;



@RestController
@RequestMapping("/api/children")
@SecurityRequirement(name = "api")
public class ChildAPI {

    @Autowired
    private ChildService childService;

    @GetMapping
    public ResponseEntity<List<ChildDTO>> getAllChildren() {
        List<ChildDTO> children = childService.getAllChildren();
        return ResponseEntity.ok(children);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChildDTO> getChildById(@PathVariable Long id) {
        ChildDTO child = childService.getChildById(id);
        return ResponseEntity.ok(child);
    }

    @PostMapping
    public ResponseEntity<ChildDTO> createChild(@RequestBody ChildDTO childDTO) {
        ChildDTO createdChild = childService.createChild(childDTO);
        return ResponseEntity.ok(createdChild);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChildDTO> updateChild(@PathVariable Long id, @RequestBody ChildDTO childDTO) {
        ChildDTO updatedChild = childService.updateChild(id, childDTO);
        return ResponseEntity.ok(updatedChild);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChild(@PathVariable Long id) {
        childService.deleteChild(id);
        return ResponseEntity.noContent().build();
    }
}
