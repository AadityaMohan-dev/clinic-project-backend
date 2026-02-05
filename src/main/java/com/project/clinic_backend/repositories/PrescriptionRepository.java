package com.project.clinic_backend.repositories;

import com.project.clinic_backend.models.entities.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {
}
