package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Invoice;
import com.dentpulse.dentalsystem.entity.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    long countByStatus(InvoiceStatus status);

    @Query("SELECT SUM(i.amount) FROM Invoice i WHERE i.status = 'PAID'")
    Double getTotalRevenue();

    List<Invoice> findByPatientFullNameContainingIgnoreCase(String keyword);

    @Query("""
        SELECT SUM(i.amount)
        FROM Invoice i
        WHERE i.invoiceDate = :today
          AND i.status = 'PAID'
    """)
    Double getTodayRevenue(LocalDate today);

}


