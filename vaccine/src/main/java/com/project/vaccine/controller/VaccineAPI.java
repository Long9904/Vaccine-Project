package com.project.vaccine.controller;

import com.project.vaccine.dto.VaccineDTO;
import com.project.vaccine.entity.Vaccine;
import com.project.vaccine.service.VaccineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/vaccine")
public class VaccineAPI {

    @Autowired
    private VaccineService vaccineService;

    @PostMapping
    public ResponseEntity<?> createVaccine(@Valid @RequestBody VaccineDTO vaccineDTO) {
        Vaccine vaccine = vaccineService.createVaccine(vaccineDTO);
        return ResponseEntity.ok(vaccine);
    }

    @GetMapping
    public ResponseEntity<?> getVaccines() {
        return ResponseEntity.ok(vaccineService.getAllVaccines());
    }
}
