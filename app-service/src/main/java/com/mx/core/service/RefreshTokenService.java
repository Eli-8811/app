package com.mx.core.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mx.core.repository.RefreshTokenRepository;
import com.mx.core.repository.entity.RefreshToken;
import com.mx.core.repository.entity.Usuario;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    @Value("${app.jwtExpirationInMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioService usuarioService;

    public RefreshToken createRefreshToken(Long userId) {
        Usuario user = usuarioService.findById(userId);
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        token.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired. Please sign in again.");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteByUserId(Long userId) {
        Usuario user = usuarioService.findById(userId);
        refreshTokenRepository.deleteByUser(user);
    }
    
}