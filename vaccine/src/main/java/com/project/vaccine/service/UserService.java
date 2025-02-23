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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDTO getUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        return modelMapperConfig.modelMapper().map(user, UserDTO.class);
    }

    public UserDTO updateCurrentUser(String username, UserRequest userRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));

        String message = "";

        if(userRepository.findByUsername(userRequest.getUsername()).isPresent()){
            message += "Username ";
        }
        if(userRepository.findByEmail(userRequest.getEmail()).isPresent()){
            message += "Email ";
        }
        if(userRepository.findByPhone(userRequest.getPhone()).isPresent()){
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
