package com.dentpulse.dentalsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.dentpulse.dentalsystem.repository.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AiRecommendationService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private AppointmentRepository appointmentRepository;

    public String getBusyStatus(int weekdayOrWeekend,
                                int month,
                                int dayOfWeek,
                                double hour) {

        LocalTime timeSlot = convertHourToLocalTime(hour);
        int pastAppointments = findPastAppointmentCount(timeSlot);

        // 🔹 Python AI service endpoint
        String url = "http://localhost:5000/predict";

        // 🔹 Request body
        Map<String, Object> body = new HashMap<>();
        body.put("weekdayOrWeekend", weekdayOrWeekend);
        body.put("month", month);
        body.put("dayOfWeek", dayOfWeek);
        body.put("hour", hour);
        body.put("pastAppointments", pastAppointments);

        // 🔹 Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 🔹 HTTP Entity
        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        // 🔹 Call AI service
        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        // 🔹 Expected response: { "busyLabel": "Low Busy" }
        return response.getBody().get("busyLabel").toString();
    }

    private int findPastAppointmentCount(LocalTime startTime) {

        // 🔹 Define date range (last 7 weeks)
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusWeeks(7);

        // 🔹 Count past appointments for the same time slot
        return appointmentRepository.countPastAppointmentsForSameTimeSlot(
                startTime,
                startDate,
                endDate
        );
    }

    // 🔹 Helper method to convert double hour → LocalTime
    private LocalTime convertHourToLocalTime(double hour) {
        int h = (int) hour;
        int m = (int) Math.round((hour - h) * 60);
        return LocalTime.of(h, m);
    }
}


