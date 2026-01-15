package com.dentpulse.dentalsystem.dto;

import lombok.Data;
import java.util.List;

@Data
public class PatientListDto {
    private Long patientId;
    private String fullName;
    private String birthDate;
    private String relationship;
    private String phone;
    private String gender;
    private String address;
    private String email;

    // ADD THIS
    private List<TreatmentRecordDTO> treatmentRecords;
}
