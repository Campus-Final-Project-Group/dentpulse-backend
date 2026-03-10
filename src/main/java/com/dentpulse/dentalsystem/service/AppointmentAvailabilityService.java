package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.entity.Appointment;
import com.dentpulse.dentalsystem.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE (MMM dd)");

        LocalDate today = LocalDate.now();

        for (int i = 0; i < 3; i++) {

            LocalDate date = today.plusDays(i);
            DayOfWeek day = date.getDayOfWeek();

            List<LocalTime> allSlots;

            // WEEKDAY NORMAL APPOINTMENTS
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {

                allSlots = generateSlots(
                        LocalTime.of(16, 0),
                        LocalTime.of(19, 30),
                        30
                );

            }
            // WEEKEND SPECIAL APPOINTMENTS
            else {

                allSlots = generateSlots(
                        LocalTime.of(10, 30),
                        LocalTime.of(16, 30),
                        120
                );
            }

            // GET APPOINTMENTS FROM DATABASE
            List<Appointment> appointments =
                    appointmentRepository.findByAppointmentDate(date);

            // FILTER BOOKED TIMES
            List<LocalTime> bookedSlots = appointments.stream()
                    .map(Appointment::getStartTime)
                    .toList();

            // REMOVE BOOKED FROM ALL
            allSlots.removeAll(bookedSlots);

            sb.append(date.format(dateFormatter)).append("\n");

            if (allSlots.isEmpty()) {

                sb.append("❌ No available slots\n\n");

            } else {

                for (LocalTime slot : allSlots) {
                    sb.append("• ").append(slot.format(timeFormatter)).append("\n");
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
