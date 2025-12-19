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
import java.util.ArrayList;
import java.util.List;

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

    public List<String> getBookedTimeSlots(String dateStr) {

        // 1. Convert string date to LocalDate
        LocalDate date = LocalDate.parse(dateStr);

        // 2. Get appointments for that date (except CANCELLED)
        List<Appointment> appointments =
                appointmentRepo.findByAppointmentDateAndStatusNot(
                        date,
                        AppointmentStatus.CANCELLED
                );

        // 3. Collect booked times using for-loop
        List<String> bookedTimes = new ArrayList<>();

        for (Appointment appointment : appointments) {
            bookedTimes.add(appointment.getStartTime().toString());
        }

        return bookedTimes;
    }

    public List<AppointmentResponseDto> getAppointmentsForUserAndFamily(String token) {
        String email = jwtUtil.extractEmail(token);  // Extract user email from JWT token
        User user = userRepo.findByEmail(email);  // Fetch user by email

        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        // Get all patients (self + family)
        List<Patient> familyMembers = patientRepo.findAllByUserId(user.getId());

        // Fetch appointments for the user and family members (including cancelled ones)
        List<Appointment> appointments = new ArrayList<>();
        for (Patient patient : familyMembers) {
            appointments.addAll(appointmentRepo.findByPatientId(patient.getId()));  // Fetch all appointments, including cancelled ones
        }


        // Convert Appointment entities to DTOs
        List<AppointmentResponseDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            AppointmentResponseDto dto = new AppointmentResponseDto();
            dto.setAppointmentId(appointment.getId());
            dto.setFullName(appointment.getPatient().getFullName());
            dto.setPatientId(appointment.getPatient().getId());
            dto.setAppointmentDate(appointment.getAppointmentDate().toString());
            dto.setStartTime(appointment.getStartTime().toString());
            dto.setStatus(appointment.getStatus().name());
            appointmentDtos.add(dto);
        }

        return appointmentDtos;
    }

    public void cancelAppointment(Long appointmentId, String token) {
        // Step 1: Extract user email from token
        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        // Step 2: Find the appointment by its ID
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Step 3: Check if the user has permission to cancel this appointment
        if (!appointment.getPatient().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("This appointment does not belong to the current user");
        }

        // Step 4: Cancel appointment
        appointmentRepo.delete(appointment);  // DELETE the appointment entirely from the database
    }


}
