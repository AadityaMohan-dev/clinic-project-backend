package com.project.clinic_backend.models.dtos;

import java.util.UUID;

public record PrescriptionRequestDto(
        UUID patientId,
        UUID doctorId,
        String medicines,
        String dosageInstructions,
        String additionalNotes
) {}
