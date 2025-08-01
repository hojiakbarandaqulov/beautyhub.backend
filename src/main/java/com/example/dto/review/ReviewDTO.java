package com.example.dto.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private String author;
    private Double rating;
    private String comment;
    private LocalDateTime createdAt;
    private Long salonId;
}