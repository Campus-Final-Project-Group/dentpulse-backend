package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.*;
import com.dentpulse.dentalsystem.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dentpulse.dentalsystem.dto.VerifyEmailRequest;


@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register-patient")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterPatientRequest dto) {
        return ResponseEntity.ok(authService.registerPatient(dto));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody VerifyEmailRequest dto) {
        authService.verifyEmail(dto);
        return ResponseEntity.ok("Email verified successfully. You can now log in.");
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequest dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<String> me() {
        return ResponseEntity.ok("Authenticated");
    }
}
