package com.dentpulse.dentalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponseDto {

    private Long invoiceId;
    private String invoiceNumber;
    private String patientName;
    private String description;
    private double amount;
    private String status;
    private String paymentMethod;
    private String invoiceDate;
}