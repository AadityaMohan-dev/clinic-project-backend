package com.project.clinic_backend.models.dtos;

import java.util.UUID;

public record MedicationResponseDto(
        UUID id,
        String name,
        String dosage,
        String frequency,
        String duration,
        String instructions
) {}
