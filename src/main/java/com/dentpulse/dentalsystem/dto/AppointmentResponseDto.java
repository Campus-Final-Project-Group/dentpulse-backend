package com.dentpulse.dentalsystem.dto;

import lombok.Data;

@Data
public class AppointmentResponseDto {

    private Long appointmentId;
    private Long patientId;
    private String appointmentDate;
    private String startTime;
    private String status;
}
