package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.review.ReviewCreateDTO;
import com.example.dto.review.ReviewResponseDTO;
import com.example.enums.LanguageEnum;
import com.example.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Review", description = "Review (baholash) larni yaratish, tahrirlash, o'chirish va ko'rish uchun APIlar")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create")
    @Operation(
            summary = "Yangi review yaratish",
            description = "Salon yoki xizmat uchun yangi review yozish. Foydalanuvchi baho va izoh qoldiradi."
    )
    public ResponseEntity<ApiResult<ReviewResponseDTO>> createReview(
            @Valid @RequestBody ReviewCreateDTO reviewDTO,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<ReviewResponseDTO> response = reviewService.createReview(reviewDTO, language);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getList")
    @Operation(
            summary = "Barcha reviewlar",
            description = "Tizimdagi barcha reviewlarni ro'yxatini qaytaradi."
    )
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        List<ReviewResponseDTO> responses = reviewService.getAllReviews();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/getBy/{id}")
    @Operation(
            summary = "Review tafsilotlari",
            description = "Berilgan id bo'yicha reviewni to'liq ma'lumotini qaytaradi."
    )
    public ResponseEntity<ReviewResponseDTO> getReviewById(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ReviewResponseDTO response = reviewService.getReviewById(id, language);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/salon/{salonId}")
    @Operation(
            summary = "Salon reviewlari",
            description = "Berilgan salon id bo'yicha shu salon uchun yozilgan barcha reviewlarni qaytaradi."
    )
    public ResponseEntity<ApiResult<List<ReviewResponseDTO>>> getReviewsBySalonId(
            @PathVariable Long salonId) {
        ApiResult<List<ReviewResponseDTO>> responses = reviewService.getReviewsBySalonId(salonId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "Reviewni yangilash",
            description = "Berilgan id bo'yicha reviewni tahrirlaydi."
    )
    public ResponseEntity<ApiResult<ReviewResponseDTO>> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewCreateDTO reviewDTO,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<ReviewResponseDTO> response = reviewService.updateReview(id, reviewDTO, language);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Reviewni o'chirish",
            description = "Berilgan id bo'yicha reviewni tizimdan o'chiradi."
    )
    public ResponseEntity<ApiResult<Boolean>> deleteReview(@PathVariable Long id,
                                                           @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<Boolean> apiResult = reviewService.deleteReview(id, language);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/salon/average-rating/{salonId}")
    @Operation(
            summary = "Salon uchun o'rtacha reyting",
            description = "Berilgan salonId bo'yicha salonning o'rtacha reytingini qaytaradi."
    )
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