package com.project.vaccine.dto.response;

import com.project.vaccine.entity.Vaccine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccineResponse {

    private String message;
    private Vaccine vaccine;
}
