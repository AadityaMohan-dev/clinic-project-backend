package com.project.clinic_backend.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "medications")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String dosage;       // e.g. 500mg
    private String frequency;    // e.g. Twice a day
    private String duration;     // e.g. 5 days
    private String instructions; // e.g. After food

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;
}
