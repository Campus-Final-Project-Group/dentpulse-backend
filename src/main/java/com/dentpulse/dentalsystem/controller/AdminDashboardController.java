package com.dentpulse.dentalsystem.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
public class AdminDashboardController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "Welcome to Admin Dashboard";
    }
}

