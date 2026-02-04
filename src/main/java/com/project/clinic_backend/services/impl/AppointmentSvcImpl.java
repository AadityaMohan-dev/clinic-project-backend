package com.project.clinic_backend.services.impl;

import com.project.clinic_backend.models.dtos.AppointmentBookingRequestDto;
import com.project.clinic_backend.models.dtos.AppointmentResponseDto;
import com.project.clinic_backend.models.entities.User;
import com.project.clinic_backend.repositories.AppointmentRepository;
import com.project.clinic_backend.services.svc.AppointmentSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentSvcImpl implements AppointmentSvc {

    private final AppointmentRepository appointmentRepository;

    @Override
    public List<AppointmentResponseDto> getAppointmentsByUser(User user) {

        return List.of();
    }

    @Override
    public List<AppointmentResponseDto> getAllAppointments() {
        return List.of();
    }

    @Override
    public AppointmentResponseDto getAppointmentById(UUID id) {
        return null;
    }

    @Override
    public AppointmentResponseDto updateAppointment(UUID id, AppointmentBookingRequestDto request) {
        return null;
    }

    @Override
    public void deleteAppointment(UUID id) {

    }

    @Override
    public String bookAppointment(AppointmentBookingRequestDto request, User user) {
        return "";
    }

    @Override
    public String createAppointmentByClinic(AppointmentBookingRequestDto request) {
        return "";
    }
}
