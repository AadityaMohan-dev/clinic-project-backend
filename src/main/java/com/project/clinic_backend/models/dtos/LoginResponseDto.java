package com.project.clinic_backend.models.dtos;

import lombok.Builder;

@Builder
public record LoginResponseDto(
    String jwt,
    String userId
) {
}
