package com.project.clinic_backend.models.dtos;

import com.project.clinic_backend.models.enums.AppointmentStatus;
import com.project.clinic_backend.models.enums.AppointmentType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Builder
public record AppointmentResponseDto(

        UUID appointmentId,

        String fullName,

        String email,

        String phoneNumber,

        LocalDate appointmentDate,

        LocalTime appointmentTime,

        String reasonForVisit,

        String doctorName ,

        String additionalNotes,

        AppointmentType appointmentType,

        AppointmentStatus status,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {}
