package com.project.vaccine.service;


import com.project.vaccine.dto.request.LoginRequest;
import com.project.vaccine.dto.request.UserRequest;
import com.project.vaccine.dto.response.LoginResponse;
import com.project.vaccine.entity.User;
import com.project.vaccine.enums.VerificationEnum;
import com.project.vaccine.exception.AuthenticationException;
import com.project.vaccine.exception.DuplicateException;
import com.project.vaccine.exception.ErrorDetail;
import com.project.vaccine.exception.NotFoundException;
import com.project.vaccine.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;


    public String register(UserRequest userRequest) {
        // Check duplicate
        List<ErrorDetail> errors = new ArrayList<>();

        if (authenticationRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            errors.add(new ErrorDetail("username", "Username already exists"));
        }

        if (authenticationRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            errors.add(new ErrorDetail("email", "Email already exists"));

        }

        if (authenticationRepository.findByPhone(userRequest.getPhone()).isPresent()) {
            errors.add(new ErrorDetail("phone", "Phone number already exists"));
        }

        if (!errors.isEmpty()) {
            throw new DuplicateException(errors);
        }

        // Create new user
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setAddress(userRequest.getAddress());
        user.setName(userRequest.getName());
        user.setGender(userRequest.getGender());
        user.setDob(userRequest.getDob());
        user.setDate_created(LocalDateTime.now());
        user.setPendingEmail(null);

        authenticationRepository.save(user);
        verificationService.createToken(user, VerificationEnum.REGISTER);
        return "Register successfully";
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authenticationRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Username not found"));
    }

    public LoginResponse login(LoginRequest loginRequest) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            User user = authenticationRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new NotFoundException("Username not found"));
            String token = tokenService.generateToken(user);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            loginResponse.setRole(user.getRole());
            loginResponse.setUsername(user.getUsername());
            loginResponse.setName(user.getName());
            loginResponse.setId(user.getId());
            return loginResponse;

        } catch (Exception e) {
            throw new AuthenticationException("Username or password is incorrect");
        }
    }
}
