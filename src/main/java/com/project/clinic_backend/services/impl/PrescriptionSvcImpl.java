package com.project.clinic_backend.services.impl;

import com.project.clinic_backend.models.dtos.MedicationResponseDto;
import com.project.clinic_backend.models.dtos.PrescriptionRequestDto;
import com.project.clinic_backend.models.dtos.PrescriptionResponseDto;
import com.project.clinic_backend.models.entities.Doctor;
import com.project.clinic_backend.models.entities.Medication;
import com.project.clinic_backend.models.entities.Patient;
import com.project.clinic_backend.models.entities.Prescription;
import com.project.clinic_backend.repositories.DoctorRepository;
import com.project.clinic_backend.repositories.PatientRepository;
import com.project.clinic_backend.repositories.PrescriptionRepository;
import com.project.clinic_backend.services.svc.PrescriptionSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrescriptionSvcImpl implements PrescriptionSvc {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    // ================= CREATE PRESCRIPTION =================
    @Override
    public String createPrescription(PrescriptionRequestDto request) {

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<Medication> medications = request.medication().getPrescription().getMedications().stream()
                .map(medDto -> Medication.builder()
                        .name(medDto.getName())
                        .dosage(medDto.getDosage())
                        .frequency(medDto.getFrequency())
                        .duration(medDto.getDuration())
                        .instructions(medDto.getInstructions())
                        .build())
                .toList();

        Prescription prescription = Prescription.builder()
                .patient(patient)
                .doctor(doctor)
                .medications(medications)
                .additionalNotes(request.additionalNotes())
                .build();

        // Set back-reference
        medications.forEach(med -> med.setPrescription(prescription));

        prescriptionRepository.save(prescription);

        return "Prescription created successfully";
    }

    // ================= GET BY ID =================
    @Override
    public PrescriptionResponseDto getPrescriptionById(UUID id) {

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        return mapToDto(prescription);
    }

    // ================= GET ALL =================
    @Override
    public List<PrescriptionResponseDto> getPrescriptions() {
        return prescriptionRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // ================= UPDATE PRESCRIPTION =================
    @Override
    public PrescriptionResponseDto updatePrescription(UUID id, PrescriptionRequestDto request) {

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        // Replace medication list
        List<Medication> updatedMedications = request.medication().getPrescription().getMedications().stream()
                .map(medDto -> Medication.builder()
                        .name(medDto.getName())
                        .dosage(medDto.getDosage())
                        .frequency(medDto.getFrequency())
                        .duration(medDto.getDuration())
                        .instructions(medDto.getInstructions())
                        .build())
                .toList();

        prescription.setMedications(updatedMedications);
        prescription.setAdditionalNotes(request.additionalNotes());

        prescriptionRepository.save(prescription);

        return mapToDto(prescription);
    }

    // ================= DELETE =================
    @Override
    public void deletePrescription(UUID id) {

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        prescriptionRepository.delete(prescription);
    }

    // ================= ENTITY → DTO =================
    private PrescriptionResponseDto mapToDto(Prescription prescription) {

        return new PrescriptionResponseDto(
                prescription.getId(),
                prescription.getPatient().getId(),
                prescription.getPatient().getUser().getName(),
                prescription.getDoctor().getId(),
                prescription.getDoctor().getUser().getName(),
                prescription.getMedications()
                        .stream()
                        .map(this::mapMedicationToDto)
                        .toList(),
                prescription.getAdditionalNotes(),
                prescription.getCreatedAt()
        );
    }

    private MedicationResponseDto mapMedicationToDto(Medication medication) {

        return new MedicationResponseDto(
                medication.getId(),
                medication.getName(),
                medication.getDosage(),
                medication.getFrequency(),
                medication.getDuration(),
                medication.getInstructions()
        );
    }
}
