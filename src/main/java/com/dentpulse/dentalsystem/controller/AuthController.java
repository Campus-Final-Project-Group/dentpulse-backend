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
        String msg = authService.verifyEmail(dto);
        return ResponseEntity.ok(msg);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequest dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<String> me() {
        return ResponseEntity.ok("Authenticated");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ) {
        authService.sendForgotPasswordOtp(request.getEmail());
        return ResponseEntity.ok("OTP sent to your email");
    }

    @PostMapping("/forgot-password/verify-otp")
    public ResponseEntity<String> verifyForgotPasswordOtp(
            @RequestBody VerifyForgotPasswordOtpRequest request
    ) {
        authService.verifyForgotPasswordOtp(
                request.getEmail(),
                request.getOtp()
        );

        return ResponseEntity.ok("OTP verified successfully");
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {
        authService.resetPassword(
                request.getEmail(),
                request.getNewPassword()
        );

        return ResponseEntity.ok("Password reset successful");
    }



}
