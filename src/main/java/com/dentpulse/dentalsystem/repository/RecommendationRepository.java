package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface RecommendationRepository extends JpaRepository<Appointment, Long> {

    @Query("""
        SELECT COUNT(a)
        FROM Appointment a
        WHERE FUNCTION('DAYOFWEEK', a.appointmentDate) = :dayOfWeek
          AND a.startTime = :time
          AND a.status = 'SCHEDULED'
    """)
    int countPastAppointments(
            @Param("dayOfWeek") int dayOfWeek,
            @Param("time") LocalTime time
    );
}