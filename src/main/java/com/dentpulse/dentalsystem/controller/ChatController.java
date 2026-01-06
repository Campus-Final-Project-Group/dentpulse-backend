package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.ChatRequest;
import com.dentpulse.dentalsystem.dto.ChatResponse;
import com.dentpulse.dentalsystem.service.ChatService;
import com.dentpulse.dentalsystem.service.ClinicInfoService;
import com.dentpulse.dentalsystem.service.AppointmentAvailabilityService;
import com.dentpulse.dentalsystem.chat.ChatSessionStore;
import com.dentpulse.dentalsystem.chat.ChatStage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    private final ChatService chatService;
    private final ClinicInfoService clinicInfoService;
    private final AppointmentAvailabilityService appointmentAvailabilityService;
    private final ChatSessionStore chatSessionStore;

    @Value("${chatbot.system-prompt}")
    private String systemPrompt;

    public ChatController(
            ChatService chatService,
            ClinicInfoService clinicInfoService,
            AppointmentAvailabilityService appointmentAvailabilityService,
            ChatSessionStore chatSessionStore
    ) {
        this.chatService = chatService;
        this.clinicInfoService = clinicInfoService;
        this.appointmentAvailabilityService = appointmentAvailabilityService;
        this.chatSessionStore = chatSessionStore;
    }

    // =========================
    // MAIN CHAT ENDPOINT
    // =========================
    @PostMapping
    public ChatResponse chat(
            @RequestBody ChatRequest request,
            @RequestHeader("X-Session-Id") String sessionId
    ) {

        String msg = request.getMessage().toLowerCase();
        ChatStage stage = chatSessionStore.getStage(sessionId);

        // 1️⃣ CLINIC INFO (TOP PRIORITY)
        if (msg.contains("location") || msg.contains("where")) {
            return new ChatResponse(clinicInfoService.getClinicLocation());
        }

        if (msg.contains("doctor")) {
            return new ChatResponse(clinicInfoService.getDoctors());
        }

        if (msg.contains("open") || msg.contains("hours") || msg.contains("time")) {
            return new ChatResponse(clinicInfoService.getOpeningHours());
        }

        if (msg.contains("service")) {
            return new ChatResponse(clinicInfoService.getServices());
        }

        // 2️⃣ GREETINGS / THANK YOU
        if (isGreeting(msg)) {
            return greetingResponse();
        }

        // 3️⃣ SERVICE → APPOINTMENT FLOW
        if (isClinicService(msg) && stage == ChatStage.NORMAL) {
            chatSessionStore.setStage(sessionId, ChatStage.ASK_APPOINTMENT_CONFIRMATION);
            return appointmentPrompt();
        }

        // 4️⃣ USER CONFIRMS
        if (stage == ChatStage.ASK_APPOINTMENT_CONFIRMATION &&
                (msg.equals("yes") || msg.equals("ok") || msg.contains("sure"))) {

            chatSessionStore.setStage(sessionId, ChatStage.SHOW_TIME_SLOTS);
            return new ChatResponse(
                    appointmentAvailabilityService.getAvailableSlotsNext7Days()
            );
        }

        // 5️⃣ OUTSIDE CLINIC SERVICES
        if (!isClinicService(msg)) {
            return outOfScopeResponse();
        }

        // 6️⃣ AI FALLBACK (SAFE GENERAL INFO)
        return new ChatResponse(
                chatService.generateReply(systemPrompt, request.getMessage())
        );
    }

    // =========================
    // HELPER METHODS
    // =========================
    private ChatResponse appointmentPrompt() {
        return new ChatResponse(
                "🦷 We provide this dental service.\n\n" +
                        "To proceed, you need to make an appointment.\n" +
                        "Would you like to see available dates and time slots?"
        );
    }

    private ChatResponse outOfScopeResponse() {
        return new ChatResponse(
                "⚠️ I’m a chatbot and cannot provide specific medical solutions.\n\n" +
                        "Please visit our dental clinic for proper consultation with a qualified dentist."
        );
    }

    private ChatResponse greetingResponse() {
        return new ChatResponse(
                "😊 You're welcome! If you need help with dental services or appointments, feel free to ask."
        );
    }

    private boolean isClinicService(String msg) {
        return msg.contains("tooth") ||
                msg.contains("extraction") ||
                msg.contains("filling") ||
                msg.contains("root canal") ||
                msg.contains("nerve") ||
                msg.contains("scaling") ||
                msg.contains("cleaning") ||
                msg.contains("gum") ||
                msg.contains("surgical");
    }

    private boolean isGreeting(String msg) {
        return msg.matches(
                ".*\\b(hi|hello|hey|good morning|good afternoon|good evening|thanks|thank you|ok|okay)\\b.*"
        );
    }
}