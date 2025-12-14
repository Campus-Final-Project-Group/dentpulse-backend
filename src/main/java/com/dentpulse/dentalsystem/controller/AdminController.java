package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.RequestAdminDto;
import com.dentpulse.dentalsystem.dto.ResponseAdminDto;
import com.dentpulse.dentalsystem.service.AdminService;
import com.dentpulse.dentalsystem.util.StandardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dentpulse/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/create")
    public ResponseEntity<StandardResponseDto> create(@RequestBody RequestAdminDto dto) {
        ResponseAdminDto response = adminService.createAdmin(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StandardResponseDto(201, "Admin Saved", response));
    }




}
