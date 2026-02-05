package com.project.clinic_backend.models.dtos;

import com.project.clinic_backend.models.enums.AppointmentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
@Builder
public record AppointmentBookingRequestDto(

        @NotBlank(message = "Full name is required")
        String fullName,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        @NotNull(message = "Appointment date is required")
        LocalDate appointmentDate,

        @NotNull(message = "Appointment time is required")
        LocalTime appointmentTime,

        @NotBlank(message = "Reason for visit is required")
        String reasonForVisit,

        String additionalNotes,

        String doctorName,

        @NotNull(message = "Appointment type is required")
        AppointmentType appointmentType
) {}

