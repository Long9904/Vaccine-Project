package com.project.vaccine.controller;

import com.project.vaccine.entity.Child;
import com.project.vaccine.service.ChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/child")
public class ChildAPI {

    @Autowired
    private ChildService childService;

    @PostMapping("/register")
    public ResponseEntity<Child> registerChild(@RequestBody Child child, @AuthenticationPrincipal UserDetails userDetails) {
        Child createdChild = childService.createChild(child);
        return ResponseEntity.ok(createdChild);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Child> getChild(@PathVariable Long id) {
        Child child = childService.getChildById(id);
        return ResponseEntity.ok(child);
    }

    @GetMapping
    public ResponseEntity<List<Child>> getAllChildren() {
        List<Child> children = childService.getAllChildren();
        return ResponseEntity.ok(children);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Child> updateChild(@PathVariable Long id, @RequestBody Child child) {
        Child updatedChild = childService.updateChild(id, child);
        return ResponseEntity.ok(updatedChild);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChild(@PathVariable Long id) {
        childService.deleteChild(id);
        return ResponseEntity.noContent().build();
    }
}