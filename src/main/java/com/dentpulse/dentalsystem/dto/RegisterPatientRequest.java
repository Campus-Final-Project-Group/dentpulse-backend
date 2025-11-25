package com.dentpulse.dentalsystem.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterPatientRequest {
    private String fullName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String password;

}
