package com.dentpulse.dentalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class WeeklyRevenueDTO {
    private LocalDate weekStartDate;
    private Double revenue;
}
