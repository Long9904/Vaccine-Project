package com.project.vaccine.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccineDetailsDTO {

    private Long id;

    @Min(value = 1, message = "Dose number must be at least 1")
    private int doseNumber;

    @Min(value = 0, message = "Interval days must be at least 0")
    private int intervalDays;

    @Min(value = 0, message = "Price must be at least 0")
    private double price;

    @Min(value = 0, message = "Min age month must be at least 0")
    @Max(value = 72, message = "Min age month must be at most 72") // 6 years
    private int minAgeMonth;

    @Min(value = 0, message = "Max age month must be at least 0")
    @Max(value = 72, message = "Max age month must be at most 72") // 6 years
    private int maxAgeMonth;

    private boolean recommended;
}
