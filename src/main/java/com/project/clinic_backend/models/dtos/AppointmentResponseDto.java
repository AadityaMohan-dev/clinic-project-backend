package com.project.clinic_backend.models.dtos;

public record AppointmentResponseDto(
        String userId,
        String message,
        String username
) {
}
