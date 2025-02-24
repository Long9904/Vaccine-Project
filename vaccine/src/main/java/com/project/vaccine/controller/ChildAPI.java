package com.project.vaccine.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/child")
public class ChildAPI {

    @PostMapping("/register")
    public void registerChild(@AuthenticationPrincipal UserDetails userDetails) {
        // register child
    }


}
