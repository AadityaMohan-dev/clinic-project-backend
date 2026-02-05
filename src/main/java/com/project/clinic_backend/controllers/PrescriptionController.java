package com.project.clinic_backend.controllers;

import com.project.clinic_backend.models.dtos.PrescriptionRequestDto;
import com.project.clinic_backend.models.dtos.PrescriptionResponseDto;
import com.project.clinic_backend.services.svc.PrescriptionSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionSvc prescriptionSvc;

    // ================= CREATE PRESCRIPTION =================
    @PostMapping
    public ResponseEntity<String> createPrescription(@RequestBody PrescriptionRequestDto request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prescriptionSvc.createPrescription(request));
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDto> getPrescriptionById(@PathVariable UUID id) {
        return ResponseEntity.ok(prescriptionSvc.getPrescriptionById(id));
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<List<PrescriptionResponseDto>> getPrescriptions() {
        return ResponseEntity.ok(prescriptionSvc.getPrescriptions());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDto> updatePrescription(
            @PathVariable UUID id,
            @RequestBody PrescriptionRequestDto request
    ) {
        return ResponseEntity.ok(prescriptionSvc.updatePrescription(id, request));
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePrescription(@PathVariable UUID id) {
        prescriptionSvc.deletePrescription(id);
        return ResponseEntity.ok("Prescription deleted successfully");
    }
}
