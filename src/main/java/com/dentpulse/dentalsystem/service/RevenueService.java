package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.dto.WeeklyRevenueDTO;
import com.dentpulse.dentalsystem.repository.InvoiceRepository;
import com.dentpulse.dentalsystem.repository.RevenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService {

    private final RevenueRepository revenueRepository;

    public List<WeeklyRevenueDTO> getWeeklyRevenue() {
        return revenueRepository.getWeeklyRevenue()
                .stream()
                .map(obj -> new WeeklyRevenueDTO(
                        ((Date) obj[0]).toLocalDate(),
                        ((Number) obj[1]).doubleValue()
                ))
                .toList();
    }
}

