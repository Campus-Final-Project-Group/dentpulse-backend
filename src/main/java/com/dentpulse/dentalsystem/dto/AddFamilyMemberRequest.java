package com.dentpulse.dentalsystem.dto;

import lombok.Data;

@Data
public class AddFamilyMemberRequest {

    private String fullName;
    private String relationship; // Spouse, Child, Parent, Other
    private String email;
    private String phone;
    private String birthDate; // yyyy-MM-dd
    private String address;
    private String gender;

    private Boolean hasNic;   // from UI radio button
    private String nic;
}
