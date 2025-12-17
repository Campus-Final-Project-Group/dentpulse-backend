package com.dentpulse.dentalsystem.dto;

import lombok.Data;

@Data
public class PatientProfileResponseDto {

    // User data
    private Long userId;
    private String fullName;
    private String email;
    private String phone;

    // Patient data (account owner)
    private Long patientId;
    private String dateOfBirth;
    private String address;
}
