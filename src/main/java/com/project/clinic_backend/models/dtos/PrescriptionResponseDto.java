package com.project.clinic_backend.models.dtos;

import com.project.clinic_backend.models.entities.Medication;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Builder
public record PrescriptionResponseDto(
        UUID id,
        UUID patientId,
        String patientName,
        UUID doctorId,
        String doctorName,
        List<MedicationResponseDto> medications,
        String additionalNotes,
        Instant createdAt
) {
}

