package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.config.JwtUtil;
import com.dentpulse.dentalsystem.dto.AppointmentResponseDto;
import com.dentpulse.dentalsystem.dto.CreateAppointmentRequest;

import com.dentpulse.dentalsystem.dto.AppointmentDTO;
import com.dentpulse.dentalsystem.entity.Appointment;
import com.dentpulse.dentalsystem.entity.AppointmentStatus;
import com.dentpulse.dentalsystem.entity.Patient;
import com.dentpulse.dentalsystem.entity.User;
import com.dentpulse.dentalsystem.repository.AppointmentRepository;
import com.dentpulse.dentalsystem.repository.PatientRepository;
import com.dentpulse.dentalsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static com.dentpulse.dentalsystem.entity.AppointmentStatus.*;


@Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PatientRepository patientRepo;


    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ModelMapper modelMapper;

    public List<AppointmentDTO> getAllAppointments() {
        List<Appointment> appointmentList = appointmentRepo.findAll();
        return modelMapper.map(appointmentList, new TypeToken<List<AppointmentDTO>>() {
                }.getType()
        );
    }

    public Map<String, Long> getAppointmentStats() {

        Map<String, Long> status = new HashMap<>();

        status.put("totalAppointments", appointmentRepo.count());
        status.put("scheduled", appointmentRepo.countByStatus(AppointmentStatus.SCHEDULED));
        status.put("completed", appointmentRepo.countByStatus(COMPLETED));
        status.put("cancelled", appointmentRepo.countByStatus(CANCELLED));

        return status;
    }

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
//        boolean alreadyBooked =
//                appointmentRepo.existsByAppointmentDateAndStartTimeAndStatusNot(
//                        date,
//                        time,
//                        AppointmentStatus.CANCELLED
//                );

        List<AppointmentStatus> blockingStatuses = List.of(
                AppointmentStatus.PENDING,
                AppointmentStatus.CONFIRMED,
                AppointmentStatus.SCHEDULED
        );

        boolean alreadyBooked =
                appointmentRepo.existsByAppointmentDateAndStartTimeAndStatusIn(
                        date,
                        time,
                        blockingStatuses
                );


        if (alreadyBooked) {
            throw new RuntimeException("This time slot is already booked");
        }

        // 5. Create appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setAppointmentDate(date);
        appointment.setStartTime(time);

        //  REMOVE these (PrePersist handles them)
        // appointment.setStatus(AppointmentStatus.PENDING);
        // appointment.setCreatedAt(LocalDateTime.now());
        // appointment.setType("Checkup");

        Appointment saved = appointmentRepo.save(appointment);

        // 6. Response
        AppointmentResponseDto dto = new AppointmentResponseDto();
        dto.setAppointmentId(saved.getId());
        dto.setPatientId(patient.getId());
        dto.setFullName(patient.getFullName());
        dto.setAppointmentDate(saved.getAppointmentDate().toString());
        dto.setStartTime(saved.getStartTime().toString());
        dto.setStatus(saved.getStatus().name());
        dto.setType(saved.getType());

        return dto;
    }

    public List<String> getBookedTimeSlots(String dateStr) {

        // 1. Convert string date to LocalDate
        LocalDate date = LocalDate.parse(dateStr);

        // 2. Get appointments for that date (except CANCELLED)
//        List<Appointment> appointments =
//                appointmentRepo.findByAppointmentDateAndStatusNot(
//                        date,
//                        AppointmentStatus.CANCELLED
//                );

        List<AppointmentStatus> blockingStatuses = List.of(
                AppointmentStatus.PENDING,
                AppointmentStatus.CONFIRMED,
                AppointmentStatus.SCHEDULED
        );

        List<Appointment> appointments =
                appointmentRepo.findByAppointmentDateAndStatusIn(date, blockingStatuses);



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
            dto.setType(appointment.getType());
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

        // Step 4: Allow delete ONLY if PENDING or CANCELLED
        if (appointment.getStatus() != AppointmentStatus.PENDING &&
                appointment.getStatus() != CANCELLED) {

            throw new RuntimeException(
                    "You can't delete this appointment. Please contact us."
            );
        }



        // Step 4: Cancel appointment
        appointmentRepo.delete(appointment);  // DELETE the appointment entirely from the database
    }

    /*    public List<AppointmentDTO> getTodayAppointments() {
        LocalDate today = LocalDate.now();

        // Convert LocalDate → java.sql.Date
        java.sql.Date sqlDate = java.sql.Date.valueOf(today);

        // Convert java.sql.Date → java.util.Date  (MATCH ENTITY)
        java.util.Date utilDate = new java.util.Date(sqlDate.getTime());

        List<Appointment> appointments =
                appointmentRepo.findByAppointment_dateOrderByStartTimeAsc(utilDate);

        return modelMapper.map(appointments,
                new TypeToken<List<AppointmentDTO>>(){}.getType());
    }*/

    public List<AppointmentResponseDto> getTodayAppointments() {

        LocalDate today = LocalDate.now();

        List<Appointment> appointments =
                appointmentRepo.findByAppointmentDate(today);

        List<AppointmentResponseDto> response = new ArrayList<>();

        for (Appointment appointment : appointments) {

            AppointmentResponseDto dto = new AppointmentResponseDto();
            dto.setAppointmentId(appointment.getId());
            dto.setPatientId(appointment.getPatient().getId());
            dto.setFullName(appointment.getPatient().getFullName());
            dto.setAppointmentDate(appointment.getAppointmentDate().toString());
            dto.setStartTime(appointment.getStartTime().toString());
            dto.setStatus(appointment.getStatus().name());
            dto.setType(appointment.getType());

            response.add(dto);
        }

        return response;
    }

    public List<AppointmentResponseDto> getAllAppointmentsForAdmin() {

        List<Appointment> appointments =
                appointmentRepo.findAllByOrderByAppointmentDateDescStartTimeAsc();

        List<AppointmentResponseDto> response = new ArrayList<>();

        for (Appointment appointment : appointments) {
            AppointmentResponseDto dto = new AppointmentResponseDto();
            dto.setAppointmentId(appointment.getId());
            dto.setPatientId(appointment.getPatient().getId());
            dto.setFullName(appointment.getPatient().getFullName());
            dto.setAppointmentDate(appointment.getAppointmentDate().toString());
            dto.setStartTime(appointment.getStartTime().toString());
            dto.setStatus(appointment.getStatus().name());
            dto.setType(appointment.getType());
            response.add(dto);
        }

        return response;
    }

    public List<AppointmentResponseDto> getAppointmentsByDate(LocalDate date) {

        List<Appointment> appointments =
                appointmentRepo.findByAppointmentDate(date);

        List<AppointmentResponseDto> response = new ArrayList<>();

        for (Appointment appointment : appointments) {
            AppointmentResponseDto dto = new AppointmentResponseDto();
            dto.setAppointmentId(appointment.getId());
            dto.setPatientId(appointment.getPatient().getId());
            dto.setFullName(appointment.getPatient().getFullName());
            dto.setAppointmentDate(appointment.getAppointmentDate().toString());
            dto.setStartTime(appointment.getStartTime().toString());
            dto.setStatus(appointment.getStatus().name());
            dto.setType(appointment.getType());
            response.add(dto);
        }

        return response;
    }


    public Map<String, Long> getAdminAppointmentStats() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("total", appointmentRepo.count());
        stats.put("scheduled",
                appointmentRepo.countByStatus(CONFIRMED)
              + appointmentRepo.countByStatus(SCHEDULED)
        );
        stats.put("completed", appointmentRepo.countByStatus(COMPLETED));
        stats.put("cancelled",
                appointmentRepo.countByStatus(CANCELLED)
              + appointmentRepo.countByStatus(NO_SHOW)
        );

        return stats;
    }

   /* public List<AppointmentResponseDto> getAllAppointmentsForAdmin() {

        List<Appointment> appointments =
                appointmentRepo.findAllByOrderByAppointmentDateDescStartTimeAsc();

        List<AppointmentResponseDto> response = new ArrayList<>();

        for (Appointment appointment : appointments) {
            AppointmentResponseDto dto = new AppointmentResponseDto();
            dto.setAppointmentId(appointment.getId());
            dto.setPatientId(appointment.getPatient().getId());
            dto.setFullName(appointment.getPatient().getFullName());
            dto.setAppointmentDate(appointment.getAppointmentDate().toString());
            dto.setStartTime(appointment.getStartTime().toString());
            dto.setStatus(appointment.getStatus().name());
            dto.setType(appointment.getType());
            response.add(dto);
        }

        return response;
    }*/

    public List<AppointmentResponseDto> searchByPatientName(String patientName) {

        List<Appointment> appointments =
                appointmentRepo.findByPatientFullNameContainingIgnoreCase(patientName);

        List<AppointmentResponseDto> response = new ArrayList<>();

        for (Appointment appointment : appointments) {

            AppointmentResponseDto dto = new AppointmentResponseDto();

            dto.setAppointmentId(appointment.getId());
            dto.setPatientId(appointment.getPatient().getId());
            dto.setFullName(appointment.getPatient().getFullName());

            // Convert LocalDate → String
            dto.setAppointmentDate(
                    appointment.getAppointmentDate().toString()
            );

            // Convert LocalTime → String
            dto.setStartTime(
                    appointment.getStartTime().toString()
            );

            dto.setStatus(appointment.getStatus().name());
            dto.setType(appointment.getType());

            response.add(dto);
        }

        return response;
    }
}
