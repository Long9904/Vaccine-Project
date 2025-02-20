package com.project.vaccine.service;


import com.project.vaccine.config.ModelMapperConfig;
import com.project.vaccine.dto.request.RegisterRequest;
import com.project.vaccine.dto.request.LoginRequest;
import com.project.vaccine.dto.response.LoginResponse;
import com.project.vaccine.entity.User;
import com.project.vaccine.enums.RoleEnum;
import com.project.vaccine.enums.UserStatusEnum;
import com.project.vaccine.exception.AuthenticationException;
import com.project.vaccine.exception.DuplicateException;
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

    @Autowired
    private ModelMapperConfig modelMapperConfig;


    public String register(RegisterRequest registerRequest) {

        User user = modelMapperConfig.modelMapper().map(registerRequest, User.class);
        try {
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRole(RoleEnum.USER);
            user.setStatus(UserStatusEnum.INACTIVE);
            authenticationRepository.save(user);
        } catch (Exception e) {
            String message = "Duplicate: ";
            if (e.getMessage().contains(user.getUsername())) {
                message += "Username";
            }
            if (e.getMessage().contains(user.getEmail())) {
                message += "Email";
            }
            if (e.getMessage().contains(user.getPhone())) {
                message += "Phone";
            }
            throw new DuplicateException(message);
        }
        return "Account created successfully";
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authenticationRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Username not found"));
    }


    public LoginResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            User user = authenticationRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
            String token = tokenService.generateAccessToken(user);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setAccessToken(token);
            loginResponse.setRole(user.getRole());
            return loginResponse;

        } catch (Exception e) {
            throw new AuthenticationException("Username or password is incorrect");
        }
    }


    public boolean testLogin(String token) {
        if (!tokenService.isTokenExpired(token)) {
            return true;
        } else {
            return false;
        }
    }

}
