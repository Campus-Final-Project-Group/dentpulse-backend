package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.ChatRequest;
import com.dentpulse.dentalsystem.dto.ChatResponse;
import com.dentpulse.dentalsystem.service.ChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    private final ChatService chatService;

    @Value("${chatbot.system-prompt}")
    private String systemPrompt;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {

        if (!isDentalQuestion(request.getMessage())) {
            return new ChatResponse(
                    "Hi 😊 I’m DentPulse Assistant. I’m here to help with dental and oral health questions. You can ask me about tooth pain, gum problems, dental care, or treatments 🦷"
            );
        }

        String reply = chatService.generateReply(
                systemPrompt,
                request.getMessage()
        );

        return new ChatResponse(reply);
    }

    private boolean isDentalQuestion(String msg) {
        String text = msg.toLowerCase();

        // Allow greetings
        if (text.matches(".*\\b(hi|hello|hey|good morning|good evening)\\b.*")) {
            return true;
        }

        return text.matches(
                ".*(tooth|teeth|dental|gum|pain|cavity|brace|mouth|oral|bleeding|swelling).*"
        );
    }

}
