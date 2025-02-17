package com.project.vaccine.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VaccineDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    private boolean status=true;

    @Min(value = 0, message = "Quantity must be at least 0")
    private long quantity;

    private LocalDateTime create_At;

    private LocalDateTime update_At;

    @Min(value = 0, message = "Age must be at least 0 months age")
    @Max(value = 72, message = "Age must be at most 72 months age")
    private int min_age;

    @Min(value = 0, message = "Age must be at least 0 months age")
    @Max(value = 72, message = "Age must be at most 72 months age")
    private int max_age;
}