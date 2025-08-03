package com.example.service;

import com.example.dto.base.ApiResult;
import com.example.dto.review.ReviewCreateDTO;
import com.example.dto.review.ReviewResponseDTO;
import com.example.entity.home_pages.Review;
import com.example.entity.home_pages.SalonEntity;
import com.example.enums.LanguageEnum;
import com.example.exp.AppBadException;
import com.example.repository.ReviewRepository;
import com.example.repository.SalonRepository;
import com.example.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SalonRepository salonRepository;
    private final ModelMapper modelMapper;
    private final ResourceBundleService messageService;

    // Yangi review yaratish
    @Transactional
    public ApiResult<ReviewResponseDTO> createReview(ReviewCreateDTO reviewDTO, LanguageEnum language) {
        // Salon mavjudligini tekshirish
        SalonEntity salon = salonRepository.findById(reviewDTO.getSalonId())
                .orElseThrow(() -> new AppBadException(messageService.getMessage("salon.not.found", language)));

        Review review = modelMapper.map(reviewDTO, Review.class);
        review.setSalon(salon);

        Review savedReview = reviewRepository.save(review);
        return ApiResult.successResponse(convertToDto(savedReview));
    }

    // Barcha reviewlarni olish
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ID bo'yicha review olish
    @Transactional(readOnly = true)
    public ReviewResponseDTO getReviewById(Long id, LanguageEnum language) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("review.not.found", language)));
        return convertToDto(review);
    }

    @Transactional(readOnly = true)
    public ApiResult<List<ReviewResponseDTO>> getReviewsBySalonId(Long salonId) {
        List<Review> reviews = reviewRepository.findBySalonId(salonId);
        List<ReviewResponseDTO> responseDTOs = new ArrayList<>();
        for (Review review : reviews) {
            ReviewResponseDTO dto = new ReviewResponseDTO();
            dto.setId(review.getId());
            dto.setAuthor(review.getAuthor());
            dto.setRating(review.getRating());
            dto.setComment(review.getComment());
            dto.setCreatedAt(review.getCreatedAt());
            dto.setSalonId(review.getSalon().getId());
            responseDTOs.add(dto);
        }
        return ApiResult.successResponse(responseDTOs);
    }

    // Reviewni yangilash
    @Transactional
    public ApiResult<ReviewResponseDTO> updateReview(Long id, ReviewCreateDTO reviewDTO, LanguageEnum language) {
        // Review mavjudligini tekshirish
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("review.not.found", language)));

        // Salon mavjudligini tekshirish
        SalonEntity salon = salonRepository.findById(reviewDTO.getSalonId())
                .orElseThrow(() -> new AppBadException(messageService.getMessage("salon.not.found", language)));

        // Yangi ma'lumotlarni o'zgartirish
        modelMapper.map(reviewDTO, existingReview);
        existingReview.setSalon(salon);

        // Saqlash va natijani qaytarish
        Review updatedReview = reviewRepository.save(existingReview);
        return ApiResult.successResponse(convertToDto(updatedReview));
    }

    // Reviewni o'chirish
    @Transactional
    public ApiResult<Boolean> deleteReview(Long id, LanguageEnum language) {
        if (!reviewRepository.existsById(id)) {
            throw new AppBadException(messageService.getMessage("review.not.found", language));
        }
        reviewRepository.deleteById(id);

        return ApiResult.successResponse(true);
    }

    // Salon uchun o'rtacha reyting
    @Transactional(readOnly = true)
    public ApiResult<Double> getAverageRatingForSalon(Long salonId) {
        Double averageRating = reviewRepository.findAverageRatingBySalonId(salonId);
        if (averageRating == null) {
            averageRating = 0.0;
        }
        return ApiResult.successResponse(averageRating);
    }

    // Entity -> DTO konvertatsiya
    private ReviewResponseDTO convertToDto(Review review) {
        ReviewResponseDTO dto = modelMapper.map(review, ReviewResponseDTO.class);
        dto.setSalonId(review.getSalon().getId());
        return dto;
    }

    public ApiResult<PageImpl<ReviewResponseDTO>> getReviews(int page, int size) {
        Long profileId = SpringSecurityUtil.getProfileId();
        Pageable pageable = PageRequest.of(page, size);
        List<Review> all = reviewRepository.findAll();
        List<ReviewResponseDTO> responseDTOs = new ArrayList<>();
        for (Review review : all) {
            ReviewResponseDTO dto = convertToDto(review);
            dto.setSalonId(review.getSalon().getId());
            responseDTOs.add(dto);
        }
        long totalReviews = reviewRepository.count();
        return ApiResult.successResponse(new PageImpl<>(responseDTOs, pageable, totalReviews));
    }
}