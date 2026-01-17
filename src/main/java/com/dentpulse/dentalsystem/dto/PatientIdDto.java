package com.dentpulse.dentalsystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientIdDto {

    private Long id;
    private String fullName;
    private String address;
    private String gender;
    private String phone;
    private LocalDate dob;

}

