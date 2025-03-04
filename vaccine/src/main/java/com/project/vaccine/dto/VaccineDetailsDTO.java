package com.project.vaccine.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccineDetailsDTO {

    private long id;

    @Min(value = 1, message = "Dose number must be at least 1")
    private int dose_number;

    @Min(value = 0, message = "Interval days must be at least 0")
    private int interval_days;

    @Min(value = 0, message = "Price must be at least 0")
    private double price;

    private boolean recommended;
}
