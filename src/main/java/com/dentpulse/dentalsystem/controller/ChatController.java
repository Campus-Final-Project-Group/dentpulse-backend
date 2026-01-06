package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.ChatRequest;
import com.dentpulse.dentalsystem.dto.ChatResponse;
import com.dentpulse.dentalsystem.service.ChatService;
import com.dentpulse.dentalsystem.service.ClinicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000") // ✅ Vite port
public class ChatController {

    private final ChatService chatService;
    private final ClinicInfoService clinicInfoService;

    @Value("${chatbot.system-prompt}")
    private String systemPrompt;

    public ChatController(ChatService chatService, ClinicInfoService clinicInfoService) {
        this.chatService = chatService;
        this.clinicInfoService = clinicInfoService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {

        String msg = request.getMessage().toLowerCase();

        //LOCATION
        if (msg.contains("location") || msg.contains("where")) {
            return new ChatResponse(clinicInfoService.getClinicLocation());
        }

        //DOCTORS
        if (msg.contains("doctor")) {
            return new ChatResponse(clinicInfoService.getDoctors());
        }

        //OPENING HOURS
        if (msg.contains("open") || msg.contains("time") || msg.contains("hours")) {
            return new ChatResponse(clinicInfoService.getOpeningHours());
        }

        //SERVICES
        if (msg.contains("service") || msg.contains("treatment")) {
            return new ChatResponse(clinicInfoService.getServices());
        }

        //NOT DENTAL QUESTION
        if (!isDentalQuestion(msg)) {
            return new ChatResponse(
                    "Hi 😊 I’m DentPulse Assistant. I’m here to help with dental and oral health questions. "
            );
        }

        //AI RESPONSE
        String reply = chatService.generateReply(systemPrompt, request.getMessage());
        return new ChatResponse(reply);
    }

    private boolean isDentalQuestion(String text) {
        if (text.matches(".*\\b(hi|hello|hey|good morning|good evening|thanks|tq|ok|yes|no|why)\\b.*")) {
            return true;
        }

        return text.matches(
                ".*(tooth|teeth|dental|gum|pain|cavity|brace|mouth|oral|bleeding|swelling).*"
        );
    }
}
