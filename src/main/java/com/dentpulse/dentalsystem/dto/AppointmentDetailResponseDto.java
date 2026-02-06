package com.dentpulse.dentalsystem.dto;

import com.dentpulse.dentalsystem.entity.AppointmentType;
import lombok.Data;

@Data
public class AppointmentDetailResponseDto {
    private Long appointmentId;
    private String appointmentDate;
    private String startTime;

    private String status;
    private AppointmentType type;

    // Patient details
    private Long patientId;
    private String patientName;
    private String patientPhone;

}
