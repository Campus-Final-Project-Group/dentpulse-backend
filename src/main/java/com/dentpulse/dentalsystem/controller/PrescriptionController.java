package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService service;

    @PostMapping("/print")
    public int printClicked() {
        return service.incrementTodayCount();
    }

    @GetMapping("/today-count")
    public int getTodayCount() {
        return service.getTodayCount();
    }
}

