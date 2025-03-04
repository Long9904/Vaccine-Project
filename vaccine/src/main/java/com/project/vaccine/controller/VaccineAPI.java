package com.project.vaccine.controller;

import com.project.vaccine.dto.VaccineDTO;
import com.project.vaccine.dto.request.VaccineUpdateRequest;
import com.project.vaccine.dto.response.VaccineResponse;
import com.project.vaccine.service.VaccineService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @PostMapping
    public ResponseEntity<?> createVaccine(@Valid @RequestBody VaccineDTO vaccineDTO) {
        VaccineResponse vaccineResponse = vaccineService.createVaccine(vaccineDTO);
        if ("Vaccine has been overridden".equals(vaccineResponse.getMessage())) {
            return ResponseEntity.ok(vaccineResponse); // 200 OK cho ghi đè
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(vaccineResponse); // 201 Created cho tạo mới
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVaccine(@PathVariable Long id) {
        vaccineService.deleteVaccine(id);
        return ResponseEntity.ok("Delete vaccine successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVaccine(@PathVariable Long id, @Valid @RequestBody VaccineUpdateRequest request) {
        return ResponseEntity.ok(vaccineService.updateVaccine(id, request));
    }

//    @PutMapping("/{id}/details/{detailsId}")
//    public ResponseEntity<?> updateVaccineDetails(@PathVariable Long vaccineId,
//                                                  @PathVariable Long detailsId,
//                                                  @Valid @RequestBody VaccineDetailsDTO vaccineDetailsDTO) {
//        return ResponseEntity.ok(vaccineService.updateVaccineDetailsByVaccineId(vaccineId, detailsId, vaccineDetailsDTO));
//    }





}
