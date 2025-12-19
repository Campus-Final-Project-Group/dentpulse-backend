package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Appointment;
import com.dentpulse.dentalsystem.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Check if a time slot is already booked (except CANCELLED)
    boolean existsByAppointmentDateAndStartTimeAndStatusNot(
            LocalDate appointmentDate,
            LocalTime startTime,
            AppointmentStatus status
    );

    // Get all booked time slots for a given date (except CANCELLED)
    List<Appointment> findByAppointmentDateAndStatusNot(
            LocalDate appointmentDate,
            AppointmentStatus status
    );

    // Get all appointments for a specific patient (including cancelled)
    List<Appointment> findByPatientId(Long patientId);
}
