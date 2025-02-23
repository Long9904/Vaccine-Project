package com.project.vaccine.controller;

import com.project.vaccine.dto.VaccineDTO;
import com.project.vaccine.entity.Vaccine;
import com.project.vaccine.service.VaccineService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("api/vaccine")
@SecurityRequirement(name = "api")
public class VaccineAPI {

    @Autowired
    private VaccineService vaccineService;

    @PostMapping
    public ResponseEntity<?> createVaccine(@Valid @RequestBody VaccineDTO vaccineDTO) {
        Vaccine vaccine = vaccineService.createVaccine(vaccineDTO);
        return ResponseEntity.ok(vaccine);
    }

    @GetMapping
    public ResponseEntity<?> getVaccinesByStatus() {
        return ResponseEntity.ok(vaccineService.getVaccinesByStatus());
    }

    @GetMapping("/vaccine/details")
    public ResponseEntity<?> getVaccineDetails() {
        return ResponseEntity.ok(vaccineService.getVaccineDetails());
    }

}
