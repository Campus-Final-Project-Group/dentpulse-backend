package com.dentpulse.dentalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private int appointment_id;
    private String patient_name;
    private Time start_time;
    private Date appointment_date;
    private Enum status;
}
