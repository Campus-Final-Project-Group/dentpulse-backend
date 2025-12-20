package com.dentpulse.dentalsystem.repository;


import com.dentpulse.dentalsystem.entity.Appointment;
import com.dentpulse.dentalsystem.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
/*
    List<Appointment> findByAppointment_dateOrderByStartTimeAsc(Date appointmentDate);
*/
    long countByStatus(AppointmentStatus status);


}
