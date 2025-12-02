package com.dentpulse.dentalsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;
import java.sql.Date;


@Table(name = "appointment")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private int appointment_id;

    @Column(name = "appointment_date",nullable = false)
    private Date appointment_date;

    @Column(name = "start_time",nullable = false)
    private Time start_time;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;


    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "patient_id",nullable = false)
    private int patient_id;

}

