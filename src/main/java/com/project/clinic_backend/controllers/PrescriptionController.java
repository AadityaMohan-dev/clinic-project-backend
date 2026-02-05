package com.project.clinic_backend.controllers;

import com.project.clinic_backend.models.dtos.PrescriptionRequestDto;
import com.project.clinic_backend.services.svc.PrescriptionSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/prescription")
@RequiredArgsConstructor
public class PrescriptionController {
    private final PrescriptionSvc prescriptionSvc;

    @PostMapping
    public ResponseEntity<String> createPrescription(@RequestBody PrescriptionRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptionSvc.createPrescription(request));
    }
}
