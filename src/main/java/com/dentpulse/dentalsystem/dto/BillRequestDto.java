package com.dentpulse.dentalsystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BillRequestDto {

    private String patientName;
    private String description;
    private Double amount;
    private LocalDate billDate;
}
