package com.project.vaccine.service;

import com.project.vaccine.dto.request.UserRequest;
import com.project.vaccine.entity.User;
import com.project.vaccine.exception.NotFoundException;
import com.project.vaccine.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {

    private final static long EXPIRATION_TIME = 1000 * 30; // 30s for test // 1000 * 60 * 60 * 24

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.secret}")
    private  String SECRET_KEY;

    private  SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);

    }


    public  String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getId()+"")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSecretKey())
                .compact();
    }

    public UserRequest getUserFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String id = claims.getSubject();
        Long userId = Long.parseLong(id);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        UserRequest userRequest = new UserRequest();
        userRequest.setId(user.getId());
        return userRequest;
    }

}
