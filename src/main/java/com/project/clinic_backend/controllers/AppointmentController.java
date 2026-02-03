package com.project.clinic_backend.controllers;


import com.project.clinic_backend.models.dtos.AppointmentBookingRequestDto;
import com.project.clinic_backend.models.dtos.AppointmentResponseDto;
import com.project.clinic_backend.models.entities.User;
import com.project.clinic_backend.services.svc.AppointmentSvc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentSvc appointmentSvc;
    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> createAppointment(
            @RequestBody AppointmentBookingRequestDto request,
            @AuthenticationPrincipal User user
    ) {

        return ResponseEntity.ok( appointmentSvc.bookAppointment(request, user));
    }
    @PostMapping("/clinic")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST)")
    public ResponseEntity<String> createAppointmentByClinic(@Valid @RequestBody AppointmentBookingRequestDto request){
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(appointmentSvc.createAppointmentByClinic(request));
    }
    @GetMapping("/my")
    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(appointmentSvc.getAppointmentsByUser(user));
    }
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable UUID id) {
        return ResponseEntity.ok(appointmentSvc.getAppointmentById(id));
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST)")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        return ResponseEntity.ok(appointmentSvc.getAllAppointments());
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST)")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(
            @PathVariable UUID id,
            @RequestBody AppointmentBookingRequestDto request
    ) {
        return ResponseEntity.ok(appointmentSvc.updateAppointment(id, request));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAppointment(@PathVariable UUID id) {
        appointmentSvc.deleteAppointment(id);
        return ResponseEntity.ok("Appointment deleted successfully");
    }

}
