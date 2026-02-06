package com.project.clinic_backend.utils;

import com.project.clinic_backend.models.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm; // Import this
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Component
public class AuthUtil {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${jwt.access-token.expiration:3600}")
    private long accessTokenExpiration;

    @Value("${spring.application.name:clinic-backend}")
    private String issuer;

    public AuthUtil(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();

        // 1. Build Claims
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenExpiration, ChronoUnit.SECONDS))
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("role", user.getRole().name())
                .claim("phoneNumber", user.getPhoneNumber())
                .claim("token_type", "access_token")
                .claim("jti", UUID.randomUUID().toString())
                .build();

        // 2. Define the Algorithm (HS256) explicitly
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        // 3. Encode with both Header and Claims
        String token = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

        log.debug("Generated access token for user: {}", user.getEmail());

        return token;
    }

    // ... rest of the methods remain the same ...
    public Long getAccessTokenExpiration() { return accessTokenExpiration; }

    public Jwt validateToken(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            log.error("Token validation failed: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid token: " + e.getMessage());
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            Instant expiry = jwt.getExpiresAt();
            return expiry != null && Instant.now().isAfter(expiry);
        } catch (JwtException e) {
            return true;
        }
    }

    public String getUserIdFromToken(String token) {
        return validateToken(token).getSubject();
    }

    public String getEmailFromToken(String token) {
        return validateToken(token).getClaimAsString("email");
    }

    public String getRoleFromToken(String token) {
        return validateToken(token).getClaimAsString("role");
    }

    public String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

    public long getTokenRemainingTime(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            Instant expiry = jwt.getExpiresAt();
            if (expiry != null) {
                return Math.max(0, Instant.now().until(expiry, ChronoUnit.SECONDS));
            }
            return 0;
        } catch (JwtException e) {
            return 0;
        }
    }
}