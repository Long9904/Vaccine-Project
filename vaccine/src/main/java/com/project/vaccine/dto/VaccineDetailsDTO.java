package com.project.vaccine.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VaccineDetailsDTO {

    @Min(value = 1, message = "Dose number must be at least 1")
    private int dose_number;

    @Min(value = 0, message = "Date After must be at least 0")
    private int date_after;

    @Min(value = 0, message = "Price must be at least 0")
    private double price;

    @NotNull(message = "Vaccine ID is required")
    private Long vaccineId;
}