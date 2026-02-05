package com.project.clinic_backend.repositories;

import com.project.clinic_backend.models.entities.Appointment;
import com.project.clinic_backend.models.entities.Doctor;
import com.project.clinic_backend.models.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment , UUID> {
    List<Appointment> findByPatient(Patient patient);
    boolean existsByDoctorAndAppointmentDateAndAppointmentTime(
            Doctor doctor,
            LocalDate date,
            LocalTime time
    );

}
