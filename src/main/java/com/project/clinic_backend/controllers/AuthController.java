package com.project.clinic_backend.controllers;

import com.project.clinic_backend.models.dtos.*;
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
    // ... other dependencies ...

    // ... login endpoint ...

    // 1. Existing Patient Signup
    @PostMapping("/signup/user")
    public ResponseEntity<SignupResponseDto> signup(
            @Valid @RequestBody SignupRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authSvc.signup(request));
    }

    // 2. New Doctor Signup Endpoint
    @PostMapping("/signup/doctor")
    public ResponseEntity<SignupResponseDto> signupDoctor(
            @Valid @RequestBody SignupRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authSvc.signupDoctor(request));
    }

    // 3. New Receptionist Signup Endpoint
    @PostMapping("/signup/receptionist")
    public ResponseEntity<SignupResponseDto> signupReceptionist(
            @Valid @RequestBody SignupRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authSvc.signupReceptionist(request));
    }

    // ... refresh, logout, validate endpoints ...
}