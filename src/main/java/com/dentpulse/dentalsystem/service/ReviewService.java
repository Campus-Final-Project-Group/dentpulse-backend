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
        //review.setStatus(ReviewStatus.PENDING);
        review.setStatus(ReviewStatus.APPROVED); // immediate visible

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


    //for patient update and delete

    public ReviewResponseDto updateReview(String token, Long reviewId, CreateReviewRequest request) {

        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Ownership check
        if (!review.getAppointment().getPatient().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot edit this review");
        }

        // Only PENDING can update
//        if (review.getStatus() != ReviewStatus.PENDING) {
//            throw new RuntimeException("Only pending reviews can be edited. Please contact clinic.");
//        }

        if (review.getStatus() == ReviewStatus.REMOVED) {
            throw new RuntimeException("Removed reviews cannot be edited.");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updated = reviewRepo.save(review);

        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setReviewId(updated.getId());
        dto.setAppointmentId(updated.getAppointment().getId());
        dto.setPatientId(updated.getAppointment().getPatient().getId());
        dto.setPatientName(updated.getAppointment().getPatient().getFullName());
        dto.setRating(updated.getRating());
        dto.setComment(updated.getComment());
        dto.setStatus(updated.getStatus());
        dto.setCreatedAt(updated.getCreatedAt());

        return dto;
    }

    public void deleteReview(String token, Long reviewId) {

        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Ownership check
        if (!review.getAppointment().getPatient().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot delete this review");
        }

        // Only PENDING can delete
//        if (review.getStatus() != ReviewStatus.PENDING) {
//            throw new RuntimeException("Only pending reviews can be deleted. Please contact clinic.");
//        }

        if (review.getStatus() == ReviewStatus.REMOVED) {
            throw new RuntimeException("Removed reviews cannot be deleted.");
        }

        // 🔥 BREAK RELATIONSHIP FIRST
        Appointment appointment = review.getAppointment();
        appointment.setReview(null);


        reviewRepo.delete(review);
    }

    //admin part__

//    public List<ReviewResponseDto> getPendingReviews() {
//
//        List<Review> reviews = reviewRepo.findByStatus(ReviewStatus.PENDING);
//
//        List<ReviewResponseDto> result = new ArrayList<>();
//
//        for (Review review : reviews) {
//
//            Appointment appointment = review.getAppointment();
//            Patient patient = appointment.getPatient();
//
//            ReviewResponseDto dto = new ReviewResponseDto();
//            dto.setReviewId(review.getId());
//            dto.setAppointmentId(appointment.getId());
//            dto.setPatientId(patient.getId());
//            dto.setPatientName(patient.getFullName());
//            dto.setRating(review.getRating());
//            dto.setComment(review.getComment());
//            dto.setStatus(review.getStatus());
//            dto.setCreatedAt(review.getCreatedAt());
//
//            result.add(dto);
//        }
//
//        return result;
//    }

//    public void approveReview(Long reviewId) {
//
//        Review review = reviewRepo.findById(reviewId)
//                .orElseThrow(() -> new RuntimeException("Review not found"));
//
//        if (review.getStatus() != ReviewStatus.PENDING) {
//            throw new RuntimeException("Only pending reviews can be approved");
//        }
//
//        review.setStatus(ReviewStatus.APPROVED);
//        reviewRepo.save(review);
//    }

//    public void rejectReview(Long reviewId) {
//
//        Review review = reviewRepo.findById(reviewId)
//                .orElseThrow(() -> new RuntimeException("Review not found"));
//
//        if (review.getStatus() != ReviewStatus.PENDING) {
//            throw new RuntimeException("Only pending reviews can be rejected");
//        }
//
//        review.setStatus(ReviewStatus.REJECTED);
//        reviewRepo.save(review);
//    }

    public void removeReview(Long reviewId) {

        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Only APPROVED reviews can be removed
        if (review.getStatus() != ReviewStatus.APPROVED) {
            throw new RuntimeException("Only approved reviews can be removed");
        }

        review.setStatus(ReviewStatus.REMOVED);
        reviewRepo.save(review);
    }


    //review -->public show
    public List<ReviewResponseDto> getApprovedReviews() {

        List<Review> reviews =
                reviewRepo.findByStatusOrderByCreatedAtDesc(ReviewStatus.APPROVED);

        List<ReviewResponseDto> result = new ArrayList<>();

        for (Review review : reviews) {

            Appointment appointment = review.getAppointment();
            Patient patient = appointment.getPatient();

            ReviewResponseDto dto = new ReviewResponseDto();
            dto.setReviewId(review.getId());
            dto.setAppointmentId(appointment.getId());
            dto.setPatientId(patient.getId());
            dto.setPatientName(patient.getFullName());
            dto.setRating(review.getRating());
            dto.setComment(review.getComment());
            dto.setStatus(review.getStatus());
            dto.setCreatedAt(review.getCreatedAt());

            result.add(dto);
        }

        return result;
    }


}