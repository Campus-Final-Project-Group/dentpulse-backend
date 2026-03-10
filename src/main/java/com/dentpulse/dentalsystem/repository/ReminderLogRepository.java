package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.ReminderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dentpulse.dentalsystem.entity.ReminderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ReminderLogRepository extends JpaRepository<ReminderLog, Long> {

    List<ReminderLog> findAllByOrderBySentAtDesc(Pageable pageable);

}
