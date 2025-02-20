package com.project.vaccine.controller;

import com.project.vaccine.dto.VaccineDTO;
import com.project.vaccine.entity.Vaccine;
import com.project.vaccine.service.VaccineService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController("api/vaccine")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class VaccineAPI {

    @Autowired
    private VaccineService vaccineService;

    @PostMapping
    public ResponseEntity<?> createVaccine(@Valid @RequestBody VaccineDTO vaccineDTO) {
        Vaccine vaccine = vaccineService.createVaccine(vaccineDTO);
        return ResponseEntity.ok(vaccine);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getVaccines() {
        return ResponseEntity.ok(vaccineService.getAllVaccines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVaccineById(@PathVariable Long id) {
        return ResponseEntity.ok(vaccineService.getVaccineById(id));
    }

    @GetMapping("/{id}/details/{detailsId}")
    public ResponseEntity<?> getVaccineDetailsById(@PathVariable Long id, @PathVariable Long detailsId) {
        return ResponseEntity.ok(vaccineService.getVaccineDetailsById(id, detailsId));
    }
}
