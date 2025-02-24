package com.project.vaccine.service;

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

    @Value("${jwt.expiration}")
    private Long EXPIRATION_TIME;

    @Autowired
    private UserRepository userRepository;


    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("role", user.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSecretKey())
                .compact();
    }

    public User getUserFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String userId = claims.getSubject();
        Long id = Long.parseLong(userId);
        return userRepository.findUserById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }
}

