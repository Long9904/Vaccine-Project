package com.project.vaccine.dto.response;

import com.project.vaccine.enums.RoleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {


    private Long id;

    private String username;

    private String name;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    private String token;

}
