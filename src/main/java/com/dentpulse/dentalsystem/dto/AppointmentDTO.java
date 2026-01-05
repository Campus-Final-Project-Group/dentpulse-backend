package com.dentpulse.dentalsystem.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long appointmentId;

    private Long patientId;
    private String fullName;
    private LocalDate appointmentDate;

    private LocalTime startTime;
    private Enum status;
}
