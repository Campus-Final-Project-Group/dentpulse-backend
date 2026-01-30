package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RevenueRepository extends JpaRepository<Invoice, Long> {

    @Query(value = """
        SELECT 
            DATE_SUB(invoice_date, INTERVAL WEEKDAY(invoice_date) DAY) AS weekStartDate,
            SUM(amount) AS totalRevenue
        FROM invoices
        WHERE status = 'PAID'
          AND invoice_date IS NOT NULL
        GROUP BY weekStartDate
        ORDER BY weekStartDate
        """, nativeQuery = true)
    List<Object[]> getWeeklyRevenue();
}
