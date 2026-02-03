package com.project.clinic_backend.models.dtos;

import lombok.Builder;

@Builder
public record SignupResponseDto(
        String userId,
        String username,
        String message
) {
}
