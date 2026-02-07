package com.project.clinic_backend.controllers;

import com.project.clinic_backend.models.dtos.*;
import com.project.clinic_backend.models.entities.RefreshToken;
import com.project.clinic_backend.models.entities.User;
import com.project.clinic_backend.services.svc.AuthSvc;
import com.project.clinic_backend.services.svc.RefreshTokenService;
import com.project.clinic_backend.utils.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthSvc authSvc;
    private final AuthUtil authUtil;
    private final RefreshTokenService refreshTokenService;

    // ================= LOGIN (Was missing in your snippet) =================
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto request
    ) {
        log.debug("Login attempt for email: {}", request.email());
        return ResponseEntity.ok(authSvc.login(request));
    }

    // ================= PATIENT SIGNUP =================
    @PostMapping("/signup/user")
    public ResponseEntity<SignupResponseDto> signup(
            @Valid @RequestBody SignupRequestDto request
    ) {
        log.debug("Signup attempt for email: {}", request.email());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authSvc.signup(request));
    }

    // ================= DOCTOR SIGNUP =================
    @PostMapping("/signup/doctor")
    public ResponseEntity<SignupResponseDto> signupDoctor(
            @Valid @RequestBody SignupRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authSvc.signupDoctor(request));
    }

    // ================= RECEPTIONIST SIGNUP =================
    @PostMapping("/signup/receptionist")
    public ResponseEntity<SignupResponseDto> signupReceptionist(
            @Valid @RequestBody SignupRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authSvc.signupReceptionist(request));
    }

    // ================= REFRESH TOKEN =================
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(
            @Valid @RequestBody RefreshTokenRequestDto request
    ) {
        log.debug("Token refresh attempt");
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.refreshToken());
        User user = refreshToken.getUser();

        String newAccessToken = authUtil.generateAccessToken(user);

        return ResponseEntity.ok(
                LoginResponseDto.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken.getToken())
                        .tokenType("Bearer")
                        .expiresIn(authUtil.getAccessTokenExpiration())
                        .userId(user.getId().toString())
                        .build()
        );
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            @Valid @RequestBody RefreshTokenRequestDto request
    ) {
        RefreshToken token = refreshTokenService.verifyRefreshToken(request.refreshToken());
        refreshTokenService.revokeToken(token);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    // ================= VALIDATE TOKEN =================
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = authUtil.extractTokenFromHeader(authHeader);

            if (authUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("valid", false, "message", "Token expired"));
            }

            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "userId", authUtil.getUserIdFromToken(token),
                    "email", authUtil.getEmailFromToken(token),
                    "role", authUtil.getRoleFromToken(token),
                    "remainingTime", authUtil.getTokenRemainingTime(token)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "message", e.getMessage()));
        }
    }
}