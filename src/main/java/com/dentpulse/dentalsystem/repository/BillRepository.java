package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Bill;
import com.dentpulse.dentalsystem.entity.TreatmentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByBillDate(LocalDate billDate);

    Optional<Bill> findByPatientIdAndTreatmentService_TreatmentType(
            Long patientId,
            TreatmentType treatmentType
    );

    List<Bill> findByPatientId(Long patientId);

    Optional<Bill> findByPatientIdAndBillDate(Long patientId, LocalDate billDate);
}
