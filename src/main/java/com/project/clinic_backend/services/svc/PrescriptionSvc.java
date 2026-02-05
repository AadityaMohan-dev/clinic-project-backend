package com.project.clinic_backend.services.svc;

import com.project.clinic_backend.models.dtos.PrescriptionRequestDto;

public interface PrescriptionSvc {
    String createPrescription(PrescriptionRequestDto request);
}
