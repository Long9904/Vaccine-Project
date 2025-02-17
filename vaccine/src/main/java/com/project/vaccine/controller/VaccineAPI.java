package com.project.vaccine.controller;

import com.project.vaccine.dto.VaccineDTO;
import com.project.vaccine.entity.Vaccine;
import com.project.vaccine.service.VaccineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/vaccine")
public class VaccineAPI {
    @Autowired
    VaccineService vaccineService;

    @GetMapping
    public ResponseEntity getAllVaccine() {
        return ResponseEntity.ok(vaccineService.getAllVaccine());
    }

    @PostMapping
    public ResponseEntity createVaccine(@Valid @RequestBody VaccineDTO vaccine) {
        Vaccine newVaccine = vaccineService.create(vaccine);
        return ResponseEntity.ok(newVaccine);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteVaccine(@PathVariable Long id) {
        Vaccine vaccine=vaccineService.delete(id);
        return ResponseEntity.ok(vaccine);
    }

    @PutMapping("{id}")
    public ResponseEntity updateVaccine(@PathVariable Long id,@Valid @RequestBody VaccineDTO vaccine) {
        Vaccine updateVaccine = vaccineService.update(id,vaccine);
        return ResponseEntity.ok(updateVaccine);
    }
}
