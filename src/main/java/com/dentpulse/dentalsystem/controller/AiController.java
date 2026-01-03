package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.AiRequestDTO;

import com.dentpulse.dentalsystem.service.AiRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AiController {

    private final AiRecommendationService aiService;

    public AiController(AiRecommendationService aiService) {
        this.aiService = aiService;
    }

    /**
     * AI Busy / Low Busy recommendation
     */
    @PostMapping("/ai-recommendation")
    public ResponseEntity<?> getAiRecommendation(@RequestBody AiRequestDTO dto) {


        String busyStatus = aiService.getBusyStatus(
                dto.getWeekdayOrWeekend(),
                dto.getMonth(),
                dto.getDayOfWeek(),
                dto.getHour()
//                dto.getPastAppointments()
        );

        return ResponseEntity.ok(
                new AiResponse(busyStatus)
        );
    }

    /**
     * Simple response wrapper
     */
    static class AiResponse {
        private String busyLabel;

        public AiResponse(String busyLabel) {
            this.busyLabel = busyLabel;
        }

        public String getBusyLabel() {
            return busyLabel;
        }

        public void setBusyLabel(String busyLabel) {
            this.busyLabel = busyLabel;
        }
    }
}
