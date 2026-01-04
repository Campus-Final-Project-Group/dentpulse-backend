package com.dentpulse.dentalsystem.dto;

import lombok.Data;

@Data
public class AiRequestDTO {
    private int weekdayOrWeekend;
    private int month;
    private int dayOfWeek;
    private double hour;
//    private int pastAppointments;
}

