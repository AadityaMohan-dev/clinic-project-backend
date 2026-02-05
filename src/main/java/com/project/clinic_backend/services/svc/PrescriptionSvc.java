package com.project.clinic_backend.services.svc;

import com.project.clinic_backend.models.dtos.PrescriptionRequestDto;
import com.project.clinic_backend.models.dtos.PrescriptionResponseDto;

import java.util.List;
import java.util.UUID;

public interface PrescriptionSvc {
    String createPrescription(PrescriptionRequestDto request);

    PrescriptionResponseDto getPrescriptionById(UUID id);

    List<PrescriptionResponseDto> getPrescriptions();

    PrescriptionResponseDto updatePrescription(UUID id, PrescriptionRequestDto request);

    void deletePrescription(UUID id);
}
