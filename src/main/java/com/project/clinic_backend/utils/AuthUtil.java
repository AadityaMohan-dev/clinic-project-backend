package com.project.clinic_backend.utils;

import com.project.clinic_backend.models.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class AuthUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey secret;

    @PostConstruct
    public void init() {
        if (secretKey == null) {
            throw new RuntimeException("JWT secret is NULL. Check application.yml");
        }
        if (secretKey.length() < 32) {
            throw new RuntimeException("JWT Secret must be at least 32 characters");
        }
        this.secret = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId().toString())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(secret, Jwts.SIG.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractUserEmail(String token) {
        return extractClaims(token).getSubject();
    }
}
