package com.dentpulse.dentalsystem.dto;

import lombok.Data;

@Data

public class PatientProfileForTableDto {

    private Long patientId;
    private String fullName;
    private String email;
    private String phone;
    private String relationship;
    private String gender;
    private boolean accountOwner;


}
