package com.project.clinic_backend.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "prescriptions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(length = 1000)
    private String dosageInstructions;

    @Column(length = 1000)
    private String additionalNotes;

    private Instant createdAt;
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medication> medications;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
