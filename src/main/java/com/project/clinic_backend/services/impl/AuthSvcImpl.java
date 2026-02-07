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
import org.springframework.security.core.AuthenticationException;
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

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        try {
            // 1. Authenticate using Spring Security Manager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            // 2. If successful, get User details
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new BadCredentialsException("User data not found"));

            // 3. Generate Tokens
            String accessToken = authUtil.generateAccessToken(user);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

            log.info("User {} logged in successfully", user.getEmail());

            return LoginResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .tokenType("Bearer")
                    .expiresIn(authUtil.getAccessTokenExpiration())
                    .userId(user.getId().toString())
                    .build();

        } catch (AuthenticationException e) {
            log.error("Login failed for email: {} - {}", request.email(), e.getMessage());
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Override
    public SignupResponseDto signup(SignupRequestDto request) {
        return registerUser(request, UserRole.PATIENT);
    }

    @Override
    public SignupResponseDto signupDoctor(SignupRequestDto request) {
        return registerUser(request, UserRole.DOCTOR);
    }

    @Override
    public SignupResponseDto signupReceptionist(SignupRequestDto request) {
        return registerUser(request, UserRole.RECEPTIONIST);
    }

    private SignupResponseDto registerUser(SignupRequestDto request, UserRole role) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .role(role)
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