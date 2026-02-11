package com.dentpulse.dentalsystem.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.dentpulse.dentalsystem.service.ReminderService;

@RestController
@RequestMapping("/api/v1/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @PostMapping("/run")
    public String runReminders() {
        return reminderService.triggerReminders();
    }
}
