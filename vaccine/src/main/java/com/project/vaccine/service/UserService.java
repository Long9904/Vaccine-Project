package com.project.vaccine.service;


import com.project.vaccine.config.ModelMapperConfig;
import com.project.vaccine.dto.UserDTO;
import com.project.vaccine.entity.User;
import com.project.vaccine.exception.NotFoundException;
import com.project.vaccine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDTO getUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        return modelMapperConfig.modelMapper().map(user, UserDTO.class);
    }
}
