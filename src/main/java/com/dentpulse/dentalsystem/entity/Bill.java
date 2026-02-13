package com.dentpulse.dentalsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String billNumber;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String status; // Paid / Unpaid

    private String paymentMethod; // Cash / Card

    private LocalDate billDate;

    @ManyToOne
    @JoinColumn(name = "treatment_service_id", nullable = false)
    private TreatmentService treatmentService;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
}