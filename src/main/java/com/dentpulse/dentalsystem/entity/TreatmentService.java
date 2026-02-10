package com.dentpulse.dentalsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "treatment_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TreatmentType treatmentType;

    @Column(nullable = false, unique = true)
    private String serviceName;
    // e.g. "Extraction (Normal)", "Composite Filling"

    @Column(nullable = false)
    private int estimatedTimeMinutes;

    @Column(nullable = false)
    private double cost;
}
