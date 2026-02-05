package com.project.clinic_backend.models.dtos;

import java.time.Instant;
import java.util.UUID;

public record PrescriptionResponseDto(
        UUID id,
        UUID patientId,
        String patientName,
        UUID doctorId,
        String doctorName,
        String medicines,
        String dosageInstructions,
        String additionalNotes,
        Instant createdAt
) {}
