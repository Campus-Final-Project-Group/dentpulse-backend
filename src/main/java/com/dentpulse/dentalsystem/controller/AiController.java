package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.AiRequestDTO;
import com.dentpulse.dentalsystem.service.AiRecommendationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiRecommendationService service;

    public AiController(AiRecommendationService service) {
        this.service = service;
    }

   @PostMapping("/recommend")
    public String recommend(@RequestBody AiRequestDTO dto) {
        return service.getBusyStatus(
                dto.getWeekdayOrWeekend(),
                dto.getMonth(),
                dto.getDayOfWeek(),
                dto.getHour(),
                dto.getPastAppointments()
        );
    }
}
