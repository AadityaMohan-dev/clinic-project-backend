package com.project.clinic_backend.services.impl;

import com.project.clinic_backend.models.dtos.LoginRequestDto;
import com.project.clinic_backend.models.dtos.SignupRequestDto;
import com.project.clinic_backend.models.dtos.LoginResponseDto;
import com.project.clinic_backend.models.dtos.SignupResponseDto;
import com.project.clinic_backend.models.entities.User;
import com.project.clinic_backend.models.enums.UserRole;
import com.project.clinic_backend.repositories.UserRepository;
import com.project.clinic_backend.services.svc.AuthSvc;
import com.project.clinic_backend.utils.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthSvcImpl implements AuthSvc {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    @Override
    public LoginResponseDto login(LoginRequestDto request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = authUtil.generateAccessToken(user);

        return LoginResponseDto.builder()
                .jwt(token)
                .userId(user.getId().toString())
                .build();
    }

    @Override
    public SignupResponseDto signup(SignupRequestDto request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .role(UserRole.PATIENT)
                .build();

        User savedUser = userRepository.save(user);

        return SignupResponseDto.builder()
                .userId(savedUser.getId().toString())
                .username(savedUser.getUsername())
                .message("Account created successfully.")
                .build();
    }

//    @Override
//    public SignupResponseDto signupDoctor(SignupRequestDto request) {
//
//        if (userRepository.existsByEmail(request.email())) {
//            throw new RuntimeException("Email already exists");
//        }
//
//        User user = User.builder()
//                .name(request.name())
//                .email(request.email())
//                .password(passwordEncoder.encode(request.password()))
//                .phoneNumber(request.phoneNumber())
//                .role(UserRole.DOCTOR)
//                .build();
//
//        User savedUser = userRepository.save(user);
//
//        return SignupResponseDto.builder()
//                .userId(savedUser.getId().toString())
//                .username(savedUser.getUsername())
//                .message("Doctor account created successfully.")
//                .build();
//    }
}
