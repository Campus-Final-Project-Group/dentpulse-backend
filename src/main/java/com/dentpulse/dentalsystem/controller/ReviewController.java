package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.CreateReviewRequest;
import com.dentpulse.dentalsystem.dto.PatientListDto;
import com.dentpulse.dentalsystem.dto.ReviewResponseDto;
import com.dentpulse.dentalsystem.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}