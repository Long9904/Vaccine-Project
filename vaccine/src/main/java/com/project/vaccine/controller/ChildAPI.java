package com.project.vaccine.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/child")
@SecurityRequirement(name = "api")
public class ChildAPI {

    @PostMapping
    public void registerChild(@AuthenticationPrincipal UserDetails userDetails) {
        // register child
    }


}
