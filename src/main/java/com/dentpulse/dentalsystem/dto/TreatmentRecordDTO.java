package com.dentpulse.dentalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentRecordDTO {

    private Long treatment_id;      // for update
    private Long patient_id;           // FK
    private Date treatment_date;       // correct name
    private String diagnosis;
    private String dentist_note;

    //NEW
    private Long dentalServiceId;
    private String dentalServiceName;
    private Double dentalServicePrice;
}

