package com.dentpulse.dentalsystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BillRequestDto {
    private Long patientId;
    private LocalDate billDate;
    private Long treatmentServiceId;
}