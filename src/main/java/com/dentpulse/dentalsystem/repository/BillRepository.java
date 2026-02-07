package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByBillDate(LocalDate billDate);
}
