package com.dentpulse.dentalsystem.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class ReminderService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String triggerReminders() {

        String url = "http://localhost:8001/send-reminders";

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, null, String.class);

        return response.getBody();
    }
}

