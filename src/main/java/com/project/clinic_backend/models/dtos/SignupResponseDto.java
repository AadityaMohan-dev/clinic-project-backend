package com.project.clinic_backend.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record SignupResponseDto(
        @JsonProperty("user_id")
        String userId,

        String username,
        String email,
        String message
) {}