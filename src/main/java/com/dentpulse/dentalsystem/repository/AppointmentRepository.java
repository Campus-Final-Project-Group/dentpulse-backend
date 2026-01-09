package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Appointment;
import com.dentpulse.dentalsystem.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Check if a time slot is already booked (except CANCELLED)
//    boolean existsByAppointmentDateAndStartTimeAndStatusNot(
//            LocalDate appointmentDate,
//            LocalTime startTime,
//            AppointmentStatus status
//    );
//
//    // Get all booked time slots for a given date (except CANCELLED)
//    List<Appointment> findByAppointmentDateAndStatusNot(
//            LocalDate appointmentDate,
//            AppointmentStatus status
//    );

    // Get all appointments for a specific patient (including cancelled)
    List<Appointment> findByPatientId(Long patientId);

    /*
    List<Appointment> findByAppointment_dateOrderByStartTimeAsc(Date appointmentDate);
    */

    /*
     Checks whether a given time slot is already blocked by an active appointment.

     A time slot is considered BLOCKED only if there exists an appointment
     on the given date and time whose status indicates the patient may still arrive
     (PENDING, CONFIRMED, or SCHEDULED).

      This is used when creating a new appointment to prevent double booking.
     */
    boolean existsByAppointmentDateAndStartTimeAndStatusIn(
            LocalDate appointmentDate,
            LocalTime startTime,
            List<AppointmentStatus> statuses
    );


    /*
     * Retrieves all appointments for a given date that actively block time slots.

     Only appointments whose status indicates the patient may still come
     (PENDING, CONFIRMED, or SCHEDULED) are returned.
     This is used to determine which time slots should appear as unavailable
     when displaying available booking times to patients.
     */

    List<Appointment> findByAppointmentDateAndStatusIn(
            LocalDate appointmentDate,
            List<AppointmentStatus> statuses
    );



    long countByStatus(AppointmentStatus status);

    // 🔹 NEW METHOD: Count past appointments for SAME time slot within date range
    @Query("""
        SELECT COUNT(a)
        FROM Appointment a
        WHERE a.startTime = :startTime
          AND a.appointmentDate BETWEEN :startDate AND :endDate
    """)
    int countPastAppointmentsForSameTimeSlot(
            @Param("startTime") LocalTime startTime,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    //Find Appointment according to the date
    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    // For table (all appointments)
    List<Appointment> findAllByOrderByAppointmentDateDescStartTimeAsc();

    //Find Appointments by Patient Name
    List<Appointment> findByPatientFullNameContainingIgnoreCase(String fullName);

}
