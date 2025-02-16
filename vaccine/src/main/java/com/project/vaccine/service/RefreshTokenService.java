package com.project.vaccine.service;

import com.project.vaccine.entity.RefreshToken;
import com.project.vaccine.entity.User;
import com.project.vaccine.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class RefreshTokenService {

    @Value("${jwt.expiration}")
    private Long refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setExpiredAt(LocalDateTime.now().plusDays(refreshTokenExpiration));
        refreshToken.setRevoked(false);
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    // check if a refresh token is valid
    public boolean isValid(RefreshToken refreshToken) {
        return !refreshToken.isRevoked() && refreshToken.getExpiredAt().isAfter(LocalDateTime.now());
    }

    // set the revoked flag to true to revoke a refresh token
    public void revokeToken(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    // delete all refresh tokens for a user when they log out
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
