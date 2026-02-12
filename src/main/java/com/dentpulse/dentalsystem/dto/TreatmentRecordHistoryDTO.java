package com.dentpulse.dentalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentRecordHistoryDTO {
    private Integer treatment_id;      // for update
    private Long patient_id;           // FK
    private Date treatment_date;       // correct name
    private String diagnosis;
    private String dentist_note;
    private String treatmentType;
    private String treatment_service;   // "Composite Filling"
    private double cost;
}
