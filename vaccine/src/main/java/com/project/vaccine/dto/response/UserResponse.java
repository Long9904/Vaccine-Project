package com.project.vaccine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String username;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private LocalDate dob;
}
