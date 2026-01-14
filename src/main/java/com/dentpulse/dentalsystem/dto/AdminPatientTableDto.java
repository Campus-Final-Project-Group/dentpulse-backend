package com.dentpulse.dentalsystem.dto;

import lombok.Data;

@Data
public class AdminPatientTableDto {
    private Long id;
    private String fullName;
    private Integer age;
    private String gender;
    private String phone;
    private boolean isActive;
}
