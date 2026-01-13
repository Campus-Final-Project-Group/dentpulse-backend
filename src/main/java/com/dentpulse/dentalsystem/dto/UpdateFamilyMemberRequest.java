package com.dentpulse.dentalsystem.dto;

import lombok.Data;

@Data
public class UpdateFamilyMemberRequest {
    private String fullName;
    private String relationship;
    private String phone;
    private String email;
    private String address;
    private String birthDate;
    private String gender;

    private Boolean hasNic;   // from UI radio button
    private String nic;
}
