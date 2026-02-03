package com.project.clinic_backend.services.svc;

import com.project.clinic_backend.models.dtos.AppointmentBookingRequestDto;
import com.project.clinic_backend.models.dtos.AppointmentResponseDto;
import com.project.clinic_backend.models.dtos.UserAppointmentResponseDto;
import com.project.clinic_backend.models.entities.User;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface AppointmentSvc {
   
    List<AppointmentResponseDto> getAppointmentsByUser(User user);

    List<AppointmentResponseDto> getAllAppointments();

    AppointmentResponseDto getAppointmentById(UUID id);

    AppointmentResponseDto updateAppointment(UUID id, AppointmentBookingRequestDto request);

    void deleteAppointment(UUID id);

    String bookAppointment(AppointmentBookingRequestDto request, User user);

    String createAppointmentByClinic(@Valid AppointmentBookingRequestDto request);
}
