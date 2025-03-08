package com.project.vaccine.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccineDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 1, message = "Quantity must be at least 1")
    private long quantity;

    @Size(min = 1, message = "At least one vaccine detail is required")
    private List<VaccineDetailsDTO> vaccineDetails;
}
