package com.dentpulse.dentalsystem.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AppointmentAvailabilityService {

    public String getAvailableSlotsNext7Days() {

        StringBuilder sb = new StringBuilder("📅 Available Time Slots (Next 3 Days)\n\n");

        LocalDate today = LocalDate.now();

        for (int i = 0; i < 3; i++) {
            LocalDate date = today.plusDays(i);
            sb.append(date).append("\n");

            sb.append("• 4:00 PM\n");
            sb.append("• 4:30 PM\n");
            sb.append("• 5:00 PM\n");
            sb.append("• 5:30 PM\n");
            sb.append("• 6:00 PM\n\n");
        }

        return sb.toString();
    }
}
