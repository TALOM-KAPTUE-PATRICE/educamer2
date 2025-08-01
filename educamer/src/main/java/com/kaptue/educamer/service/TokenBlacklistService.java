package com.kaptue.educamer.service;

import com.kaptue.educamer.entity.TokenBlacklist;
import com.kaptue.educamer.jwt.JwtUtil;
import com.kaptue.educamer.repository.TokenBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;


@Service
public class TokenBlacklistService {

    @Autowired private TokenBlacklistRepository tokenBlacklistRepository;
    @Autowired private JwtUtil jwtUtil;

    public void blacklistToken(String token) {
        Instant expiryDate = jwtUtil.extractExpiration(token).toInstant();
        TokenBlacklist blacklistedToken = new TokenBlacklist(token, expiryDate);
        tokenBlacklistRepository.save(blacklistedToken);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }

    // Tâche planifiée pour nettoyer les tokens expirés de la blacklist toutes les heures
    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanupExpiredTokens() {
        tokenBlacklistRepository.deleteByExpiryDateBefore(Instant.now());
    }
}
