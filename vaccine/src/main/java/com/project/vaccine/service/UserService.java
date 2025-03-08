package com.project.vaccine.service;


import com.project.vaccine.config.ModelMapperConfig;
import com.project.vaccine.dto.UserDTO;
import com.project.vaccine.dto.request.UserRequest;
import com.project.vaccine.entity.User;
import com.project.vaccine.exception.DuplicateException;
import com.project.vaccine.exception.NotFoundException;
import com.project.vaccine.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ModelMapperConfig modelMapperConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapperConfig.modelMapper().map(user, UserDTO.class))
                .collect(Collectors.toList());
    }


    public UserDTO getUserProfile(String username, String email) {

        if (username != null && email == null) { // If user logs in with username and password
            User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
            UserDTO userDTO = modelMapperConfig.modelMapper().map(user, UserDTO.class);
            userDTO.setPassword(null);
            userDTO.setId(null);
            userDTO.setRole(null);
            return userDTO;

        } else if (email != null && username == null) { // If user logs in with OAuth2
            User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
            UserDTO userDTO = modelMapperConfig.modelMapper().map(user, UserDTO.class);
            userDTO.setPassword(null);
            userDTO.setId(null);
            userDTO.setRole(null);
            return userDTO;

        } else { // If user is not logged in
            throw new NotFoundException("User not found");
        }
    }

    public UserDTO updateCurrentUser(String username, UserRequest userRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        // get user details from token

        String message = "";

        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            message += "Username ";
        }
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            message += "Email ";
        }
        if (userRepository.findByPhone(userRequest.getPhone()).isPresent()) {
            message += "Phone ";
        }
        if (!message.isEmpty()) {
            throw new DuplicateException(message);
        }
        modelMapperConfig.modelMapper().map(userRequest, user);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(user);
        return modelMapperConfig.modelMapper().map(user, UserDTO.class);
    }


}
