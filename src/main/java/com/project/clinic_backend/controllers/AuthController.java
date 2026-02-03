package com.project.clinic_backend.controllers;

import com.project.clinic_backend.models.dtos.*;
import com.project.clinic_backend.models.entities.RefreshToken;
import com.project.clinic_backend.models.entities.User;
import com.project.clinic_backend.services.svc.AuthSvc;
import com.project.clinic_backend.services.svc.RefreshTokenService;
import com.project.clinic_backend.utils.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthSvc authSvc;
    private final AuthUtil authUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto request
    ) {
        return ResponseEntity.ok(authSvc.login(request));
    }

    @PostMapping("/signup/user")
    public ResponseEntity<SignupResponseDto> signup(
            @Valid @RequestBody SignupRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authSvc.signup(request));
    }
//    @PostMapping("/signup/doctor")
//    public ResponseEntity<SignupResponseDto> signupDoctor(
//            @Valid @RequestBody SignupRequestDto request
//    ) {
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(authSvc.signupDoctor(request));
//    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody RefreshTokenRequestDto request) {

        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.refreshToken());

        User user = refreshToken.getUser();
        String newAccessToken = authUtil.generateAccessToken(user);

        return ResponseEntity.ok(
                LoginResponseDto.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken.getToken())
                        .userId(user.getId().toString())
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequestDto request) {

        RefreshToken token = refreshTokenService.verifyRefreshToken(request.refreshToken());
        refreshTokenService.revokeToken(token);

        return ResponseEntity.ok("Logged out successfully");
    }


}
