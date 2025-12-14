package com.dentpulse.dentalsystem.dto;

import com.dentpulse.dentalsystem.entity.Role;
import lombok.Data;

@Data
public class RequestAdminDto {
    private String userName;
    private String email;
    private String password;
    private String contactNumber;
}
