package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Review;
import com.dentpulse.dentalsystem.entity.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //  Check if review already exists for appointment
    boolean existsByAppointmentId(Long appointmentId);

    //  Get review by appointment
    Optional<Review> findByAppointmentId(Long appointmentId);

    //  Get all reviews by status (Admin moderation)
    List<Review> findByStatus(ReviewStatus status);

    //  Get only approved reviews (Public page)
    List<Review> findByStatusOrderByCreatedAtDesc(ReviewStatus status);

    //  Get reviews by patient (through appointment)
    List<Review> findByAppointmentPatientId(Long patientId);
}