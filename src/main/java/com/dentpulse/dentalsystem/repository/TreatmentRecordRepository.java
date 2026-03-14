package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.TreatmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TreatmentRecordRepository extends JpaRepository<TreatmentRecord, Long> {
    List<TreatmentRecord> findByPatientId(Long patientId);
    // NEW - for treatment table view
    List<TreatmentRecord> findAll();

    @Query("""
    SELECT 
        t.treatmentType,
        COUNT(t)
    FROM TreatmentRecord t
    GROUP BY t.treatmentType
    """)
    List<Object[]> getTreatmentStats();
}



