package com.dentpulse.dentalsystem.dto;

import lombok.Data;

@Data
public class PatientProfileDto {
    private String fullName;
    private String email;
    private String phone;
    private String birthDate;
    private String address;
    private String gender;

    private Boolean hasNic;   // from UI radio button
    private String nic;

}
