package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.RequestAdminDto;
import com.dentpulse.dentalsystem.dto.ResponseAdminDto;
import com.dentpulse.dentalsystem.service.AdminService;
import com.dentpulse.dentalsystem.util.StandardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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


    @PutMapping("/{id}")
    public ResponseEntity<StandardResponseDto> update(@PathVariable Long id,   @RequestBody RequestAdminDto dto) throws SQLException {
        adminService.updateAdmin(dto,id);
        return new ResponseEntity<StandardResponseDto>(
                new StandardResponseDto(201,"Admin Updated",null),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponseDto> delete(@PathVariable Long id) throws SQLException {
        adminService.deleteAdmin(id);
        return new ResponseEntity<StandardResponseDto>(
                new StandardResponseDto(204,"User Deleted",null),
                HttpStatus.NO_CONTENT
        );
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponseDto> activateAdmin(@PathVariable Long id) {
        adminService.activateAdmin(id);
        return ResponseEntity.ok(
                new StandardResponseDto(200, "Admin activated successfully", null)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<StandardResponseDto> findById(@PathVariable Long id) throws SQLException {
        return new ResponseEntity<>(
                new StandardResponseDto(200,"User(Admin) Found", adminService.findByAdminId(id)),
                HttpStatus.OK
        );
    }

    @GetMapping()
    public ResponseEntity<StandardResponseDto> findAll() throws SQLException {
        return new ResponseEntity<>(
                new StandardResponseDto(200,"Admins List", adminService.findAllAdmins()),
                HttpStatus.OK
        );
    }

}
