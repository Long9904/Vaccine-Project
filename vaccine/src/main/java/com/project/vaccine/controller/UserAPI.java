package com.project.vaccine.controller;

import com.project.vaccine.dto.UserDTO;
import com.project.vaccine.dto.request.UserRequest;
import com.project.vaccine.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "api")
public class UserAPI {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> userProfile(@AuthenticationPrincipal Object userDetails) {
        String username = null;
        String email = null;
        if (userDetails instanceof UserDetails) {
            username = ((UserDetails) userDetails).getUsername();
        } else if (userDetails instanceof OAuth2User) {
            email = ((OAuth2User) userDetails).getAttribute("email");
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserDTO user = userService.getUserProfile(username, email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateCurrentUser(@RequestBody @Valid UserRequest userRequest,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        UserDTO updatedUser = userService.updateCurrentUser(userDetails.getUsername(), userRequest);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
