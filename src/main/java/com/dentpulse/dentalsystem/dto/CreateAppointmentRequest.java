package com.dentpulse.dentalsystem.dto;

import com.dentpulse.dentalsystem.entity.AppointmentType;
import com.dentpulse.dentalsystem.entity.TreatmentType;
import lombok.Data;

@Data
public class CreateAppointmentRequest {

    // Selected patient id (self or family)
    private Long patientId;

    // Selected date (ex: 2025-12-16)
    private String appointmentDate;

    // Selected time slot (ex: 09:00)
    private String startTime;

    private AppointmentType appointmentType;
    private TreatmentType treatmentType;
}
