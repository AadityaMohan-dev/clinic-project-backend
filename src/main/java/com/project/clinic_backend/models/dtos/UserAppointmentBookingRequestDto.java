package com.project.clinic_backend.models.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record UserAppointmentBookingRequestDto(
        @NotNull(message = "Appointment date is required")
        LocalDate appointmentDate,

        @NotNull(message = "Appointment time is required")
        LocalTime appointmentTime
) {}
