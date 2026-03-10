package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Bill;
import com.dentpulse.dentalsystem.entity.TreatmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT COALESCE(SUM(b.amount),0) FROM Bill b WHERE b.billDate = :date")
    Double getTodayRevenue(@Param("date") LocalDate date);
}
