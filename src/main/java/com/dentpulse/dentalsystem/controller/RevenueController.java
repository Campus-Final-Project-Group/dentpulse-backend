package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.WeeklyRevenueDTO;
import com.dentpulse.dentalsystem.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/revenue")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping("/weekly")
    public List<WeeklyRevenueDTO> getWeeklyRevenue() {
        return revenueService.getWeeklyRevenue();
    }
}

