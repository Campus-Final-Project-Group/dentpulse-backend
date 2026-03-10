package com.dentpulse.dentalsystem.dto;

import lombok.Data;

@Data
public class UpdateReviewRequest {

    private Integer rating;

    private String comment;
}