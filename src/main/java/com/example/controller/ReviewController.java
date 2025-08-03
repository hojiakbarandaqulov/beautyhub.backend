package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.review.ReviewCreateDTO;
import com.example.dto.review.ReviewResponseDTO;
import com.example.enums.LanguageEnum;
import com.example.service.ReviewService;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // POST /api/reviews - Yangi review yaratish
    @PostMapping("/create")
    public ResponseEntity<ApiResult<ReviewResponseDTO>> createReview(
            @Valid @RequestBody ReviewCreateDTO reviewDTO,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<ReviewResponseDTO> response = reviewService.createReview(reviewDTO, language);
        return ResponseEntity.ok(response);
    }

    // GET /api/reviews - Barcha reviewlar
    @GetMapping("/getList")
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        List<ReviewResponseDTO> responses = reviewService.getAllReviews();
        return ResponseEntity.ok(responses);
    }

    // GET /api/reviews/{id} - ID bo'yicha review
    @GetMapping("/getBy/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ReviewResponseDTO response = reviewService.getReviewById(id, language);
        return ResponseEntity.ok(response);
    }

    // GET /api/reviews/salon/{salonId} - Salon reviewlari
    @GetMapping("/salon/{salonId}")
    public ResponseEntity<ApiResult<List<ReviewResponseDTO>>> getReviewsBySalonId(
            @PathVariable Long salonId) {
        ApiResult<List<ReviewResponseDTO>> responses = reviewService.getReviewsBySalonId(salonId);
        return ResponseEntity.ok(responses);
    }

    // PUT /api/reviews/{id} - Reviewni yangilash
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResult<ReviewResponseDTO>> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewCreateDTO reviewDTO,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<ReviewResponseDTO> response = reviewService.updateReview(id, reviewDTO, language);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/reviews/{id} - Reviewni o'chirish
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResult<Boolean>> deleteReview(@PathVariable Long id,
                                                           @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<Boolean> apiResult = reviewService.deleteReview(id, language);
        return ResponseEntity.ok(apiResult);
    }

    // GET /api/reviews/salon/{salonId}/average-rating - O'rtacha reyting
    @GetMapping("/salon/average-rating/{salonId}")
    public ResponseEntity<ApiResult<Double>> getAverageRatingForSalon(
            @PathVariable Long salonId) {
        ApiResult<Double> averageRating = reviewService.getAverageRatingForSalon(salonId);
        return ResponseEntity.ok(averageRating);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MASTER', 'SALON_MANAGER')")
    @GetMapping("/api/v1/reviews/top-salons/")
    public ResponseEntity<ApiResult<PageImpl<ReviewResponseDTO>>> getSalonForTop(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.getReviews(page-1, size));
    }
}