package com.project.clinic_backend.services.svc;

import com.project.clinic_backend.models.dtos.LoginRequestDto;
import com.project.clinic_backend.models.dtos.LoginResponseDto;
import com.project.clinic_backend.models.dtos.SignupRequestDto;
import com.project.clinic_backend.models.dtos.SignupResponseDto;
import jakarta.validation.Valid;

public interface AuthSvc {
    LoginResponseDto login(@Valid LoginRequestDto request);

    SignupResponseDto signup(@Valid SignupRequestDto request);

//    SignupResponseDto signupDoctor(@Valid SignupRequestDto request);
}
