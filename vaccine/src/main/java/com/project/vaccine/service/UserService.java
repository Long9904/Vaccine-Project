package com.project.vaccine.service;


import com.project.vaccine.entity.User;
import com.project.vaccine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}
