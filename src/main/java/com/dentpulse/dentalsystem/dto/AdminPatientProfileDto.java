package com.dentpulse.dentalsystem.dto;

import lombok.Data;
import org.stringtemplate.v4.ST;

@Data
public class AdminPatientProfileDto {
    private Long id;
    private String fullName;
    private Integer age;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private String nic;
}
