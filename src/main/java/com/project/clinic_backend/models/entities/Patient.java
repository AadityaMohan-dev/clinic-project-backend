package com.project.clinic_backend.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "patients")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diagnosis> diagnoses;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prescription> prescriptions;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
