package com.dentpulse.dentalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminPatientTableDto {
    private Long id;
    private String fullName;
    private Integer age;
    private String gender;
    private String phone;
}
