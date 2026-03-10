package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.CreateReviewRequest;
import com.dentpulse.dentalsystem.dto.PatientListDto;
import com.dentpulse.dentalsystem.dto.ReviewResponseDto;
import com.dentpulse.dentalsystem.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;


@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // ✅ Patient creates review (only after COMPLETED appointment)
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateReviewRequest request
    ) {
        return ResponseEntity.ok(
                reviewService.createReview(token.substring(7), request)
        );
    }


    @GetMapping("/eligible-patients")
    public ResponseEntity<List<PatientListDto>> getEligiblePatients(
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(
                reviewService.getReviewEligiblePatients(token.substring(7))
        );
    }

    //update and delete by patient before approved

    // ✏️ Update review
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @RequestHeader("Authorization") String token,
            @PathVariable Long reviewId,
            @Valid @RequestBody CreateReviewRequest request
    ) {
        return ResponseEntity.ok(
                reviewService.updateReview(token.substring(7), reviewId, request)
        );
    }

    // 🗑 Delete review
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @RequestHeader("Authorization") String token,
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(token.substring(7), reviewId);
        return ResponseEntity.ok("Review deleted successfully");
    }

    //admin part___

//    @GetMapping("/admin/pending")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<ReviewResponseDto>> getPendingReviews() {
//        return ResponseEntity.ok(
//                reviewService.getPendingReviews()
//        );
//    }

    // ✅ Approve review
//    @PutMapping("/admin/{reviewId}/approve")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<?> approveReview(@PathVariable Long reviewId) {
//        reviewService.approveReview(reviewId);
//        return ResponseEntity.ok("Review approved successfully");
//    }

    // ❌ Reject review
//    @PutMapping("/admin/{reviewId}/reject")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<?> rejectReview(@PathVariable Long reviewId) {
//        reviewService.rejectReview(reviewId);
//        return ResponseEntity.ok("Review rejected successfully");
//    }

    // 🔥 Remove approved review (soft delete)
    @PutMapping("/admin/{reviewId}/remove")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeReview(@PathVariable Long reviewId) {
        reviewService.removeReview(reviewId);
        return ResponseEntity.ok("Review removed successfully");
    }

    //all review get for public

    // 🌍 Public reviews (no auth needed)
    @GetMapping("/public")
    public ResponseEntity<List<ReviewResponseDto>> getApprovedReviews() {
        return ResponseEntity.ok(
                reviewService.getApprovedReviews()
        );
    }
}