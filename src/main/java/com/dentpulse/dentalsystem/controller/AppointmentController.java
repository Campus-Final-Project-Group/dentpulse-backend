package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.AppointmentResponseDto;
import com.dentpulse.dentalsystem.dto.CreateAppointmentRequest;
import com.dentpulse.dentalsystem.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointment")
@CrossOrigin
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Create new appointment
    @PostMapping
    public ResponseEntity<AppointmentResponseDto> createAppointment(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateAppointmentRequest request
    ) {
        return ResponseEntity.ok(
                appointmentService.createAppointment(token.substring(7), request)
        );
    }
}
