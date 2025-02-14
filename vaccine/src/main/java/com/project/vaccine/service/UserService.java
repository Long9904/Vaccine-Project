package com.project.vaccine.service;


import com.project.vaccine.dto.request.UserRequest;
import com.project.vaccine.dto.response.UserResponse;
import com.project.vaccine.entity.User;
import com.project.vaccine.exception.NotFoundException;
import com.project.vaccine.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
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

    public UserResponse getUserFromToken(String token) {

        try{
            UserRequest userRequest = tokenService.getUserFromToken(token);
            User user = userRepository.findById(userRequest.getId()).orElseThrow(()
                    -> new NotFoundException("User not found"));

            UserResponse userResponse = new UserResponse();
            userResponse.setName(user.getName());
            userResponse.setUsername(user.getUsername());
            userResponse.setEmail(user.getEmail());
            userResponse.setDob(user.getDob());
            userResponse.setGender(user.getGender());
            userResponse.setPhone(user.getPhone());
            userResponse.setAddress(user.getAddress());

            return userResponse;
        }catch (ExpiredJwtException e){
            throw new ExpiredJwtException(null, null, "JWT expired");
        }
    }

}
