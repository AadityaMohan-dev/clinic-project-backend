package com.project.clinic_backend.services.impl;

import com.project.clinic_backend.models.dtos.PrescriptionResponseDto;
import com.project.clinic_backend.models.entities.Prescription;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionSvcImpl {

    private PrescriptionResponseDto mapToDto(Prescription prescription) {
        return new PrescriptionResponseDto(
                prescription.getId(),
                prescription.getPatient().getId(),
                prescription.getPatient().getUser().getName(),
                prescription.getDoctor().getId(),
                prescription.getDoctor().getUser().getName(),
                prescription.getMedicines(),
                prescription.getDosageInstructions(),
                prescription.getAdditionalNotes(),
                prescription.getCreatedAt()
        );
    }

}
