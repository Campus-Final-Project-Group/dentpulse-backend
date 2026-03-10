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

        //RESET CHAT
        if (msg.contains("restart") || msg.contains("start again") || msg.contains("reset")) {
            chatSessionStore.clear(sessionId);
            return new ChatResponse(
                    "🔄 Chat restarted.\n\nHow can I help you today?"
            );
        }

        //EMERGENCY DETECTION
        if(msg.contains("bleeding") || msg.contains("swelling") || msg.contains("emergency")){
            return new ChatResponse(
                    "⚠️ This may be a dental emergency.\n\n" +
                            "Please contact the clinic immediately.\n\n" +
                            "📞 +94 71 546 6337"
            );
        }

        //CLINIC INFO
        if (msg.contains("location") || msg.contains("where")) {
            return new ChatResponse(clinicInfoService.getClinicLocation());
        }

        if (msg.contains("doctor") || msg.contains("dentist")) {
            return new ChatResponse(clinicInfoService.getDoctors());
        }

        if (msg.contains("open") || msg.contains("hours") || msg.contains("opening")) {
            return new ChatResponse(clinicInfoService.getOpeningHours());
        }

        if (msg.contains("service")) {
            return new ChatResponse(clinicInfoService.getServices());
        }

        //GREETINGS
        if (isGreeting(msg)) {
            return greetingResponse();
        }

        if (isThankYou(msg)) {
            return new ChatResponse("😊 You're welcome! Let me know if you need help.");
        }



        //SERVICE → APPOINTMENT FLOW
        if (isClinicService(msg) && stage == ChatStage.NORMAL) {
            chatSessionStore.setStage(sessionId, ChatStage.ASK_APPOINTMENT_CONFIRMATION);
            return appointmentPrompt();
        }

        //USER CONFIRMS
        if (stage == ChatStage.ASK_APPOINTMENT_CONFIRMATION) {

            if (isConfirmation(msg)) {
                chatSessionStore.setStage(sessionId, ChatStage.SHOW_TIME_SLOTS);
                return new ChatResponse(
                        appointmentAvailabilityService.getAvailableSlotsNext3Days()
                );
            }

            chatSessionStore.setStage(sessionId, ChatStage.NORMAL);

            return new ChatResponse(
                    "No problem. Let me know if you need help with appointments or dental services."
            );
        }

        // AI FALLBACK
        try {
            return new ChatResponse(
                    chatService.generateReply(systemPrompt, request.getMessage())
            );
        } catch (Exception e) {
            return new ChatResponse(
                    "I'm sorry, I couldn't process that question. Please ask about dental services or appointments."
            );
        }
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
                "👋 Hello! I'm the DentPulse Assistant.\n\n" +
                        "You can ask me about:\n" +
                        "📍 Clinic location\n" +
                        "👨‍⚕️ Doctors\n" +
                        "⏰ Opening hours\n" +
                        "🦷 Dental services\n" +
                        "📅 Available appointments\n\n" +
                        "How can I help you today?"
        );
    }

    private boolean isClinicService(String msg) {
        return msg.contains("tooth pain") ||
                msg.contains("toothache") ||
                msg.contains("extraction") ||
                msg.contains("filling") ||
                msg.contains("root canal") ||
                msg.contains("scaling") ||
                msg.contains("cleaning") ||
                msg.contains("gum pain") ||
                msg.contains("surgical");
    }

    private boolean isGreeting(String msg) {
        return msg.matches(".*\\b(hi|hello|hey|good morning|good afternoon|good evening)\\b.*");
    }

    private boolean isThankYou(String msg) {
        return msg.matches(".*\\b(thanks|thank you|thankyou|thx)\\b.*");
    }

    private boolean isConfirmation(String msg) {
        return msg.matches(".*\\b(yes|ok|okay|sure|yep)\\b.*");
    }
}