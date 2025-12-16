package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Appointment;
import com.dentpulse.dentalsystem.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Check if a time slot is already booked (except CANCELLED)
    boolean existsByAppointmentDateAndStartTimeAndStatusNot(
            LocalDate appointmentDate,
            LocalTime startTime,
            AppointmentStatus status
    );
}
