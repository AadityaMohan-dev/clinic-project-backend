package com.project.clinic_backend.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "diagnoses")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Diagnosis belongs to a patient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Diagnosis created by a doctor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false, length = 255)
    private String diagnosisName;

    @Column(length = 500)
    private String doctorSuggestion;

    @Column(length = 1000)
    private String keyNotesAboutPatient;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}

