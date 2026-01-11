package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.AdminDashboardSummaryDto;
import com.dentpulse.dentalsystem.service.AdminDashboardService;
import com.dentpulse.dentalsystem.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService dashboardService;




    @GetMapping("/summary")
    public AdminDashboardSummaryDto getDashboardSummary() {
        return dashboardService.getDashboardSummary();
    }
}