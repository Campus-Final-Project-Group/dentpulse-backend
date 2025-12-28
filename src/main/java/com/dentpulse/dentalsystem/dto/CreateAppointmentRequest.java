package com.dentpulse.dentalsystem.dto;

import lombok.Data;

@Data
public class CreateAppointmentRequest {

    // Selected patient id (self or family)
    private Long patientId;

    // Selected date (ex: 2025-12-16)
    private String appointmentDate;

    // Selected time slot (ex: 09:00)
    private String startTime;
}
