package com.project.vaccine.controller;

import com.project.vaccine.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicAPI {
    // TODO: Implement public APIs
    // List of public APIs

    @Autowired
    private VaccineService vaccineService;

    @GetMapping("/vaccines")
    public ResponseEntity<?> getAllActiveVaccines() {
        return ResponseEntity.ok(vaccineService.getAllActiveVaccines());
    }
}
