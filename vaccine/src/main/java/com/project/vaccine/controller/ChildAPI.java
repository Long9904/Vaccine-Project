package com.project.vaccine.controller;

import com.project.vaccine.dto.ChildDTO;
import com.project.vaccine.entity.Child;
import com.project.vaccine.service.ChildService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/children")
@SecurityRequirement(name = "api")
public class ChildAPI {

    @Autowired
    private ChildService childService;

    @PostMapping
    public ResponseEntity<?> createChild(@Valid @RequestBody ChildDTO childDTO) {
        childDTO = childService.createChild(childDTO);
        return ResponseEntity.ok(childDTO);
    }
}


