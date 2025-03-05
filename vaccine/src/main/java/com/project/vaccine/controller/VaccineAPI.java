package com.project.vaccine.controller;

import com.project.vaccine.dto.VaccineDTO;
import com.project.vaccine.dto.VaccineDetailsDTO;
import com.project.vaccine.dto.response.VaccineResponse;
import com.project.vaccine.service.VaccineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vaccine")
@SecurityRequirement(name = "api")
public class VaccineAPI {

    @Autowired
    private VaccineService vaccineService;

    @Operation(
            summary = "Note: This is a note for the API",
            description = "You don't need to provide id when creating a new vaccine"
    )
    @PostMapping
    public ResponseEntity<?> createVaccine(@Valid @RequestBody VaccineDTO vaccineDTO) {
        VaccineResponse vaccineResponse = vaccineService.createVaccine(vaccineDTO);
        return ResponseEntity.ok(vaccineResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVaccine(@PathVariable Long id) {
        vaccineService.deleteVaccine(id);
        return ResponseEntity.ok("Delete vaccine successfully");
    }

    @DeleteMapping("/{vaccineId}/details/{detailsId}")
    public ResponseEntity<?> deleteVaccineDetails(@PathVariable Long vaccineId, @PathVariable Long detailsId) {
        vaccineService.deleteVaccineDetailsByVaccineId(vaccineId, detailsId);
        return ResponseEntity.ok("Delete vaccine details successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVaccine(@PathVariable Long id, @Valid @RequestBody VaccineDTO vaccineDTO) {
        return ResponseEntity.ok(vaccineService.updateVaccine(id, vaccineDTO));
    }

    @PutMapping("/{vaccineId}/details/{detailsId}")
    public ResponseEntity<?> updateVaccineDetails(@PathVariable Long vaccineId,
                                                  @PathVariable Long detailsId,
                                                  @Valid @RequestBody VaccineDetailsDTO vaccineDetailsDTO) {

        VaccineDetailsDTO newVaccineDetailsDTO = vaccineService.updateVaccineDetailsByVaccineId(vaccineId, detailsId, vaccineDetailsDTO);
        return ResponseEntity.ok(newVaccineDetailsDTO);

    }







}
