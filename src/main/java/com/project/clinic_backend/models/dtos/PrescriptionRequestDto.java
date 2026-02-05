package com.project.clinic_backend.models.dtos;

import com.project.clinic_backend.models.entities.Medication;

import java.util.UUID;

public record PrescriptionRequestDto(
        UUID patientId,
        UUID doctorId,
        Medication medication,
        String dosageInstructions,
        String additionalNotes
) {}
