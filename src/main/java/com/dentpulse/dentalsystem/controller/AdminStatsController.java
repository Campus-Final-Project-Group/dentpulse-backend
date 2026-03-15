package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.service.AdminStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class AdminStatsController {

    private final AdminStatsService statsService;

    @GetMapping("/monthly-revenue")
    public List<Map<String,Object>> monthlyRevenue(){

        return statsService.getMonthlyRevenue();
    }

    @GetMapping("/appointment-times")
    public List<Map<String,Object>> appointmentTimes(){

        return statsService.getAppointmentTimes();
    }

    @GetMapping("/treatment-stats")
    public List<Map<String,Object>> treatmentStats(){

        return statsService.getTreatmentStats();
    }

    @GetMapping("/appointments-by-day")
    public List<Map<String,Object>> appointmentsByDay(){

        return statsService.getAppointmentsByDay();
    }
}