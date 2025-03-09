package com.project.vaccine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildResponse {

    private Long id;
    private String name;
    private String gender;
    private LocalDate dob;
    private double weight;
    private double height;
    private String note;
    private Long parentId;
    private String parentName;
}
