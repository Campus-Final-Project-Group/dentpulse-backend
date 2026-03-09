package com.dentpulse.dentalsystem.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.dentpulse.dentalsystem.service.ReminderService;
import com.dentpulse.dentalsystem.repository.ReminderLogRepository;
import com.dentpulse.dentalsystem.entity.ReminderLog;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;
    private final ReminderLogRepository reminderLogRepository;

    @PostMapping("/run")
    public String runReminders() {
        return reminderService.triggerReminders();
    }

    @GetMapping("/logs")
    public List<ReminderLog> getReminderLogs() {
        return reminderLogRepository.findAllByOrderBySentAtDesc();
    }
}