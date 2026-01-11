package com.dentpulse.dentalsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String invoiceNumber;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private String description;

    private double amount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status; // PAID, PENDING

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // CASH, CARD

    private LocalDate invoiceDate;

    @PrePersist
    public void prePersist() {
        this.invoiceDate = LocalDate.now();
        this.invoiceNumber = "INV-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0,4);
        this.status = InvoiceStatus.PENDING;
    }

}