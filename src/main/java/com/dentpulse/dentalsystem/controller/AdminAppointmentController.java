package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.AppointmentResponseDto;
import com.dentpulse.dentalsystem.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/appointments")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminAppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentService.getAllAppointmentsForAdmin();
    }

    // Filter by date
    @GetMapping("/by-date")
    public List<AppointmentResponseDto> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  date)
    {
        return appointmentService.getAppointmentsByDate(date);
    }

    // Summary cards
    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        return appointmentService.getAdminAppointmentStats();
    }
}