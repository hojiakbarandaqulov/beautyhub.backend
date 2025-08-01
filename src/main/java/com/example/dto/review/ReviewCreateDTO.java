package com.example.dto.review;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewCreateDTO {
    @NotBlank(message = "Author name cannot be empty")
    private String author;

    @NotNull(message = "Rating cannot be null")
    @DecimalMin(value = "0.5", message = "Rating must be at least 0.5")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5.0")
    private Double rating;

    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;

    @NotNull(message = "Salon ID cannot be null")
    private Long salonId;
}