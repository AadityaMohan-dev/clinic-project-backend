package com.project.clinic_backend.services.svc;

import com.project.clinic_backend.models.dtos.LoginRequestDto;
import com.project.clinic_backend.models.dtos.LoginResponseDto;
import com.project.clinic_backend.models.dtos.SignupRequestDto;
import com.project.clinic_backend.models.dtos.SignupResponseDto;

public interface AuthSvc {
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    SignupResponseDto signup(SignupRequestDto signupRequestDto);
}
