package com.dentpulse.dentalsystem.dto;

import lombok.Data;

@Data
public class FamilyMemberDto {
    private Long patientId;
    private String fullName;
    private String email;
    private String phone;
    private String relationship;
    private String gender;
    private String birthDate;
    private String address;
    private boolean accountOwner;
}
