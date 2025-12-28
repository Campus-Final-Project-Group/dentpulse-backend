package com.dentpulse.dentalsystem.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
public class AiRecommendationService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getBusyStatus(int weekdayOrWeekend,
                                int month,
                                int dayOfWeek,
                                int hour,
                                int pastAppointments) {

        String url = "http://localhost:5000/predict";

        Map<String, Object> body = new HashMap<>();
        body.put("weekdayOrWeekend", weekdayOrWeekend);
        body.put("month", month);
        body.put("dayOfWeek", dayOfWeek);
        body.put("hour", hour);
        body.put("pastAppointments", pastAppointments);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        return response.getBody().get("busyLabel").toString();
    }
}

