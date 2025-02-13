package com.project.vaccine.service;


import com.project.vaccine.dto.UserDTO;
import com.project.vaccine.entity.User;
import com.project.vaccine.enums.UserStatusEnum;
import com.project.vaccine.enums.VerificationEnum;
import com.project.vaccine.exception.AuthenticationException;
import com.project.vaccine.exception.DuplicateException;
import com.project.vaccine.exception.ErrorDetail;
import com.project.vaccine.exception.NotFoundException;
import com.project.vaccine.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String register(UserDTO userDTO) {
        // Check duplicate
        List<ErrorDetail> errors = new ArrayList<>();

        if (authenticationRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            errors.add(new ErrorDetail("username", "Username is existence"));
        }

        if (authenticationRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            errors.add(new ErrorDetail("email", "Email is existence"));

        }

        if (authenticationRepository.findByPhone(userDTO.getPhone()).isPresent()) {
            errors.add(new ErrorDetail("phone", "Phone number is existence"));
        }

        if (!errors.isEmpty()) {
            throw new DuplicateException(errors);
        }

        // Create new user
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setName(userDTO.getName());
        user.setGender(userDTO.getGender());
        user.setDob(userDTO.getDob());
        user.setDate_created(LocalDateTime.now());
        user.setPendingEmail(null);

        authenticationRepository.save(user);
        verificationService.createToken(user, VerificationEnum.REGISTER);

        // JWT token
        return "Register successfully";
    }



    public UserDTO login(String username, String password) {
        User user = authenticationRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Username is not correct"));

        if (!user.getPassword().equals(password)) {
            throw new AuthenticationException("Password is not correct");
        }

        if (user.getStatus().equals(UserStatusEnum.INACTIVE)) {
            throw new AuthenticationException("Account is not verified");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setAddress(user.getAddress());
        userDTO.setName(user.getName());
        userDTO.setGender(user.getGender());
        userDTO.setPassword(null);

        //JWT token

        return userDTO;
    }


    public void logout(String username) {
        // remove session
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authenticationRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Username not found"));
    }
}
