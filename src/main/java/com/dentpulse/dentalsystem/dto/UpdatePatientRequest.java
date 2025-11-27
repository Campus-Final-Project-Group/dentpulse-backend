package com.dentpulse.dentalsystem.dto;

import lombok.Data;

@Data
public class UpdatePatientRequest {
    private String fullName;
    private String phone;
    private String birthDate;
    private String address;
}
