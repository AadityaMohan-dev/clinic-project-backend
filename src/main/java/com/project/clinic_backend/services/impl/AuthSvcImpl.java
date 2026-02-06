package com.project.clinic_backend.services.impl;

import com.project.clinic_backend.models.dtos.LoginRequestDto;
import com.project.clinic_backend.models.dtos.LoginResponseDto;
import com.project.clinic_backend.models.dtos.SignupRequestDto;
import com.project.clinic_backend.models.dtos.SignupResponseDto;
import com.project.clinic_backend.models.entities.RefreshToken;
import com.project.clinic_backend.models.entities.User;
import com.project.clinic_backend.models.enums.UserRole;
import com.project.clinic_backend.repositories.UserRepository;
import com.project.clinic_backend.services.svc.AuthSvc;
import com.project.clinic_backend.services.svc.RefreshTokenService;
import com.project.clinic_backend.utils.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthSvcImpl implements AuthSvc {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final RefreshTokenService refreshTokenService;

    // ... login method remains the same ...
    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        // ... your existing login logic ...
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = authUtil.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(authUtil.getAccessTokenExpiration())
                .userId(user.getId().toString())
                .build();
    }

    // 1. PATIENT SIGNUP
    @Override
    public SignupResponseDto signup(SignupRequestDto request) {
        return registerUser(request, UserRole.PATIENT);
    }

    // 2. DOCTOR SIGNUP
    @Override
    public SignupResponseDto signupDoctor(SignupRequestDto request) {
        return registerUser(request, UserRole.DOCTOR);
    }

    // 3. RECEPTIONIST SIGNUP
    @Override
    public SignupResponseDto signupReceptionist(SignupRequestDto request) {
        return registerUser(request, UserRole.RECEPTIONIST);
    }

    // PRIVATE HELPER METHOD TO AVOID DUPLICATION
    private SignupResponseDto registerUser(SignupRequestDto request, UserRole role) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .role(role) // Use the role passed as argument
                .build();

        User savedUser = userRepository.save(user);

        log.info("New {} registered: {}", role, savedUser.getEmail());

        return SignupResponseDto.builder()
                .userId(savedUser.getId().toString())
                .username(savedUser.getName())
                .email(savedUser.getEmail())
                .message(role.name() + " account created successfully.")
                .build();
    }
}