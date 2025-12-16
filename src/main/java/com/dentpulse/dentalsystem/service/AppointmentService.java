package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.config.JwtUtil;
import com.dentpulse.dentalsystem.dto.AppointmentResponseDto;
import com.dentpulse.dentalsystem.dto.CreateAppointmentRequest;
import com.dentpulse.dentalsystem.entity.Appointment;
import com.dentpulse.dentalsystem.entity.AppointmentStatus;
import com.dentpulse.dentalsystem.entity.Patient;
import com.dentpulse.dentalsystem.entity.User;
import com.dentpulse.dentalsystem.repository.AppointmentRepository;
import com.dentpulse.dentalsystem.repository.PatientRepository;
import com.dentpulse.dentalsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    public AppointmentResponseDto createAppointment(
            String token,
            CreateAppointmentRequest request
    ) {

        // 1. Get logged-in user
        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 2. Get patient and validate ownership
        Patient patient = patientRepo
                .findByIdAndUserId(request.getPatientId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // 3. Convert date & time
        LocalDate date = LocalDate.parse(request.getAppointmentDate());
        LocalTime time = LocalTime.parse(request.getStartTime());

        // 4. Check slot availability
        boolean alreadyBooked =
                appointmentRepo.existsByAppointmentDateAndStartTimeAndStatusNot(
                        date,
                        time,
                        AppointmentStatus.CANCELLED
                );

        if (alreadyBooked) {
            throw new RuntimeException("This time slot is already booked");
        }

        // 5. Create appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setAppointmentDate(date);
        appointment.setStartTime(time);
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setCreatedAt(LocalDateTime.now());

        Appointment saved = appointmentRepo.save(appointment);

        // 6. Response
        AppointmentResponseDto dto = new AppointmentResponseDto();
        dto.setAppointmentId(saved.getId());
        dto.setPatientId(patient.getId());
        dto.setAppointmentDate(saved.getAppointmentDate().toString());
        dto.setStartTime(saved.getStartTime().toString());
        dto.setStatus(saved.getStatus().name());

        return dto;
    }
}
