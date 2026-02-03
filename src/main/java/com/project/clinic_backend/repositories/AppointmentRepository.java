package com.project.clinic_backend.repositories;

import com.project.clinic_backend.models.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment , UUID> {
}
