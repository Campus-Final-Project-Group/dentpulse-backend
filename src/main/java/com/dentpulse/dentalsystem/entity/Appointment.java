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


@Table(name = "appointment")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @Column(name = "appointment_date",nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "start_time",nullable = false)
    private LocalTime startTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "patient_id",nullable = false)
    private int patientId;

}

