package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.config.JwtUtil;
import com.dentpulse.dentalsystem.dto.CreateReviewRequest;
import com.dentpulse.dentalsystem.dto.ReviewResponseDto;
import com.dentpulse.dentalsystem.entity.*;
import com.dentpulse.dentalsystem.repository.AppointmentRepository;
import com.dentpulse.dentalsystem.repository.PatientRepository;
import com.dentpulse.dentalsystem.repository.ReviewRepository;
import com.dentpulse.dentalsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.dentpulse.dentalsystem.dto.PatientListDto;

@Service
@Transactional
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PatientRepository patientRepo;


    public ReviewResponseDto createReview(String token, CreateReviewRequest request) {

        // 1️⃣ Get logged-in user
        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 2️⃣ Get appointment
        Appointment appointment = appointmentRepo.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // 3️⃣ Check ownership (appointment belongs to this user)
        if (!appointment.getPatient().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot review this appointment");
        }

        // 4️⃣ Check appointment status = COMPLETED
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new RuntimeException("Review allowed only after completed appointment");
        }

        // 5️⃣ Check review already exists
        if (reviewRepo.existsByAppointmentId(appointment.getId())) {
            throw new RuntimeException("Review already submitted for this appointment");
        }

        // 6️⃣ Create review
        Review review = new Review();
        review.setAppointment(appointment);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setStatus(ReviewStatus.PENDING);

        Review saved = reviewRepo.save(review);

        // 7️⃣ Map to response DTO
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setReviewId(saved.getId());
        dto.setAppointmentId(appointment.getId());
        dto.setPatientId(appointment.getPatient().getId());
        dto.setPatientName(appointment.getPatient().getFullName());
        dto.setRating(saved.getRating());
        dto.setComment(saved.getComment());
        dto.setStatus(saved.getStatus());
        dto.setCreatedAt(saved.getCreatedAt());

        return dto;
    }

    public List<PatientListDto> getReviewEligiblePatients(String token) {

        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        List<Patient> patients = patientRepo.findAllByUserId(user.getId());
        List<PatientListDto> result = new ArrayList<>();

        for (Patient patient : patients) {

            boolean hasEligibleAppointment =
                    appointmentRepo.existsByPatientIdAndStatusAndReviewIsNull(
                            patient.getId(),
                            AppointmentStatus.COMPLETED
                    );

            if (hasEligibleAppointment) {

                PatientListDto dto = new PatientListDto();
                dto.setPatientId(patient.getId());
                dto.setFullName(patient.getFullName());
                dto.setBirthDate(
                        patient.getDateOfBirth() != null
                                ? patient.getDateOfBirth().toString()
                                : null
                );

                //  relationship logic
                dto.setRelationship(
                        patient.isAccountOwner() ? "You" : patient.getRelationship()
                );

                result.add(dto);
            }
        }

        return result;
    }


}