package com.example.dto.salon;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {

    private Long id;
    private String clientName;
    private Double rating;
    private String comment;
    private LocalDateTime createdAt;
}