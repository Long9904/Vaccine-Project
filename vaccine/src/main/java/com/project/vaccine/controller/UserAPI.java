package com.project.vaccine.controller;

import com.project.vaccine.dto.UserDTO;
import com.project.vaccine.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/user")
@SecurityRequirement(name = "api")
public class UserAPI {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> userProfile(@AuthenticationPrincipal UserDetails userDetails) {
        UserDTO userDTO = userService.getUser(userDetails.getUsername());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
