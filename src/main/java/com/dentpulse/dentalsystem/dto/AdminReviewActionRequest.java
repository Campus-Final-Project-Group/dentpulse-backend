package com.dentpulse.dentalsystem.dto;

import com.dentpulse.dentalsystem.entity.ReviewStatus;
import lombok.Data;

@Data
public class AdminReviewActionRequest {

    private ReviewStatus status; // APPROVED or REJECTED
}