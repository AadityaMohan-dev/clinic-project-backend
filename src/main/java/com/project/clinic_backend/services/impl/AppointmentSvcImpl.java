package com.project.clinic_backend.services.impl;

import com.project.clinic_backend.models.dtos.AppointmentBookingRequestDto;
import com.project.clinic_backend.models.dtos.AppointmentResponseDto;
import com.project.clinic_backend.models.entities.Appointment;
import com.project.clinic_backend.models.entities.Doctor;
import com.project.clinic_backend.models.entities.Patient;
import com.project.clinic_backend.models.entities.User;
import com.project.clinic_backend.models.enums.AppointmentStatus;
import com.project.clinic_backend.models.enums.UserRole;
import com.project.clinic_backend.repositories.AppointmentRepository;
import com.project.clinic_backend.repositories.DoctorRepository;
import com.project.clinic_backend.repositories.PatientRepository;
import com.project.clinic_backend.services.svc.AppointmentSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentSvcImpl implements AppointmentSvc {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder encoder;

    // ========================= GET USER APPOINTMENTS =========================
    @Override
    public List<AppointmentResponseDto> getAppointmentsByUser(User user) {

        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        List<Appointment> appointments = appointmentRepository.findByPatient(patient);

        return appointments.stream()
                .map(this::mapToDto)
                .toList();
    }

    // ========================= GET ALL APPOINTMENTS =========================
    @Override
    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(this::mapToDto).toList();

    }

    // ========================= GET BY ID =========================
    @Override
    public AppointmentResponseDto getAppointmentById(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        return mapToDto(appointment);
    }

    // ========================= UPDATE APPOINTMENT =========================
    @Override
    public AppointmentResponseDto updateAppointment(UUID id, AppointmentBookingRequestDto request) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Doctor doctor = doctorRepository.findByUser_Name(request.doctorName())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.appointmentDate());
        appointment.setAppointmentTime(request.appointmentTime());
        appointment.setReasonForVisit(request.reasonForVisit());
        appointment.setAdditionalNotes(request.additionalNotes());
        appointment.setAppointmentType(request.appointmentType());

        appointmentRepository.save(appointment);

        return mapToDto(appointment);
    }

    // ========================= DELETE =========================
    @Override
    public void deleteAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found or already deleted"));

        appointmentRepository.delete(appointment);
    }

    // ========================= PATIENT BOOK APPOINTMENT =========================
    @Override
    public String bookAppointment(AppointmentBookingRequestDto request, User user) {

        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        Doctor doctor = doctorRepository.findByUser_Name(request.doctorName())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        // Optional: prevent double booking
        boolean slotTaken = appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(
                doctor,
                request.appointmentDate(),
                request.appointmentTime()
        );

        if (slotTaken) {
            throw new RuntimeException("Doctor already has an appointment at this time");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.appointmentDate())
                .appointmentTime(request.appointmentTime())
                .reasonForVisit(request.reasonForVisit())
                .additionalNotes(request.additionalNotes())
                .appointmentType(request.appointmentType())
                .status(AppointmentStatus.PENDING)
                .build();

        appointmentRepository.save(appointment);

        return "Appointment booked successfully. Awaiting confirmation.";
    }

    // ========================= CLINIC CREATE APPOINTMENT =========================
    @Override
    public String createAppointmentByClinic(AppointmentBookingRequestDto request) {

        Doctor doctor = doctorRepository.findByUser_Name(request.doctorName())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findByUserEmail(request.email())
                .orElseGet(() -> createPatient(request));

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.appointmentDate())
                .appointmentTime(request.appointmentTime())
                .reasonForVisit(request.reasonForVisit())
                .additionalNotes(request.additionalNotes())
                .appointmentType(request.appointmentType())
                .status(AppointmentStatus.CONFIRMED)
                .build();

        appointmentRepository.save(appointment);

        return "Appointment created successfully by clinic";
    }

    // ========================= CREATE PATIENT =========================
    private Patient createPatient(AppointmentBookingRequestDto request) {

        User user = User.builder()
                .name(request.fullName())
                .email(request.email())
                .password(encoder.encode(request.fullName() + "@" + request.phoneNumber()))
                .phoneNumber(request.phoneNumber())
                .role(UserRole.PATIENT)
                .build();

        return patientRepository.save(
                Patient.builder()
                        .user(user)
                        .build()
        );
    }

    // ========================= MAP ENTITY TO DTO =========================
    private AppointmentResponseDto mapToDto(Appointment appointment) {

        return AppointmentResponseDto.builder()
                .appointmentId(appointment.getId())
                .fullName(appointment.getPatient().getUser().getName())
                .email(appointment.getPatient().getUser().getEmail())
                .phoneNumber(appointment.getPatient().getUser().getPhoneNumber())
                .doctorName(appointment.getDoctor() != null
                        ? appointment.getDoctor().getUser().getName()
                        : null)
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .reasonForVisit(appointment.getReasonForVisit())
                .additionalNotes(appointment.getAdditionalNotes())
                .appointmentType(appointment.getAppointmentType())
                .status(appointment.getStatus())
                .build();
    }
}
