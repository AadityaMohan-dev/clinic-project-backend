package com.project.clinic_backend.repositories;

import com.project.clinic_backend.models.entities.Patient;
import com.project.clinic_backend.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Optional<Patient> findByUser(User user);

    Optional<Patient> findByUserEmail(String email);
    Optional<Patient> findByUser_Email(String email);


}
