package com.dentpulse.dentalsystem.dto;

import com.dentpulse.dentalsystem.entity.AppointmentType;
import lombok.Data;

@Data
public class AppointmentResponseDto {

    private Long appointmentId;
    private Long patientId;
    private String appointmentDate;
    private String startTime;
    private String status;
    private String fullName;

    private AppointmentType type;
}
