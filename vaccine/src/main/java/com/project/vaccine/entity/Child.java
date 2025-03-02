package com.project.vaccine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be 'Male', 'Female', or 'Other'")
    @NotBlank(message = "Gender is required")
    private String gender;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Invalid date of birth")
    private LocalDate dob;

    @Min(value = 0, message = "Weight must be greater than or equal to 0")
    private double weight;

    @Min(value = 0, message = "Height must be greater than or equal to 0")
    private double height;

    @Size(max = 255, message = "Note cannot exceed 255 characters")
    private String note;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
