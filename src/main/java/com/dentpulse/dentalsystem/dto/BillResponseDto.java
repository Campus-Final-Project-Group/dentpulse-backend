package com.dentpulse.dentalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillResponseDto {

    private Long id;
    private String billNumber;
    private String patientName;
    private String description;
    private Double amount;
    private String paymentMethod;
    private String billDate;
}
