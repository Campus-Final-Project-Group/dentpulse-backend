package com.dentpulse.dentalsystem.dto;

import com.dentpulse.dentalsystem.entity.ReviewStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponseDto {

    private Long reviewId;

    private Long appointmentId;

    private Long patientId;

    private String patientName;

    private Integer rating;

    private String comment;

    private ReviewStatus status;

    private LocalDateTime createdAt;
}