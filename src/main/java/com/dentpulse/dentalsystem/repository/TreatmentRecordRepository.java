package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.TreatmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentRecordRepository extends JpaRepository<TreatmentRecord, Long> {
}
