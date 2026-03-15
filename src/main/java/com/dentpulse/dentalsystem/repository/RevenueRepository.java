package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Bill;
import com.dentpulse.dentalsystem.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RevenueRepository extends JpaRepository<Bill, Long> {

    @Query(value = """
    SELECT 
        DATE_SUB(bill_date, INTERVAL WEEKDAY(bill_date) DAY) AS weekStartDate,
        SUM(amount) AS totalRevenue
    FROM bills
    WHERE bill_date IS NOT NULL
    GROUP BY weekStartDate
    ORDER BY weekStartDate
    """, nativeQuery = true)
    List<Object[]> getWeeklyRevenue();
}
