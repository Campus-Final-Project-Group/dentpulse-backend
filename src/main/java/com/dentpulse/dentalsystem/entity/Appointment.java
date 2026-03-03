package com.dentpulse.dentalsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;
import java.time.LocalTime;

@Entity
@Table(name = "appointment",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_appointment_slot",
                columnNames = {"appointment_date", "start_time"}
        )
)
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    //  JUST STRING (admin will update later)
    /*@Column(name = "type", nullable = false)
    private String type;
    */

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentType appointmentType  = AppointmentType.NORMAL;

    @Column(nullable = false)
    private Integer durationMinutes = 30;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TreatmentType treatmentType = TreatmentType.CHECKUP;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Review review;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = AppointmentStatus.PENDING;
        if (appointmentType == null) appointmentType = AppointmentType.NORMAL;
        if (durationMinutes == null) durationMinutes = 30;
        if (treatmentType == null) treatmentType = TreatmentType.CHECKUP;
    }
}

