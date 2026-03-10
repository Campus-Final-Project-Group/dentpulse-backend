package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.entity.Appointment;
import com.dentpulse.dentalsystem.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentAvailabilityService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentAvailabilityService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // MAIN METHOD
    public String getAvailableSlotsNext3Days() {

        StringBuilder sb = new StringBuilder("📅 Available Time Slots\n\n");

        LocalDate today = LocalDate.now();

        for (int i = 0; i < 3; i++) {

            LocalDate date = today.plusDays(i);
            DayOfWeek day = date.getDayOfWeek();

            List<LocalTime> allSlots;

            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {

                // WEEKDAY NORMAL APPOINTMENT
                allSlots = generateSlots(
                        LocalTime.of(16,0),
                        LocalTime.of(19,30),
                        30
                );

            } else {

                // WEEKEND SPECIAL APPOINTMENT
                allSlots = generateSlots(
                        LocalTime.of(10,30),
                        LocalTime.of(16,30),
                        120
                );
            }

            List<Appointment> appointments =
                    appointmentRepository.findByAppointmentDate(date);

            List<LocalTime> booked =
                    appointments.stream()
                            .map(Appointment::getStartTime)
                            .toList();

            allSlots.removeAll(booked);

            if (!allSlots.isEmpty()) {

                sb.append(date).append("\n");

                for (LocalTime slot : allSlots) {
                    sb.append("• ").append(slot).append("\n");
                }

                sb.append("\n");
            }
        }

        return sb.toString();
    }


    //SLOT GENERATOR FUNCTION
    private List<LocalTime> generateSlots(LocalTime start, LocalTime end, int duration) {

        List<LocalTime> slots = new ArrayList<>();

        LocalTime time = start;

        while (!time.plusMinutes(duration).isAfter(end)) {
            slots.add(time);
            time = time.plusMinutes(duration);
        }

        return slots;
    }

}
