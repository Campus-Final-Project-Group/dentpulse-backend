package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.AppointmentResponseDto;
import com.dentpulse.dentalsystem.dto.CreateAppointmentRequest;
import com.dentpulse.dentalsystem.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
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

    @GetMapping("/booked-times")
    public ResponseEntity<List<String>> getBookedTimeSlots(
            @RequestParam String date
    ) {
        return ResponseEntity.ok(
                appointmentService.getBookedTimeSlots(date)
        );
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments(@RequestHeader("Authorization") String token) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsForUserAndFamily(token.substring(7));
        return ResponseEntity.ok(appointments);
    }

    // Cancel an appointment (DELETE)
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable Long appointmentId,
            @RequestHeader("Authorization") String token
    ) {
        appointmentService.cancelAppointment(appointmentId, token.substring(7));
        return ResponseEntity.ok("Appointment has been cancelled successfully");
    }

}
