package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.ReminderLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderLogRepository
        extends JpaRepository<ReminderLog, Long> {
}
