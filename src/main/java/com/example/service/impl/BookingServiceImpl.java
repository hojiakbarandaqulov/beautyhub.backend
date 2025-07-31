package com.example.service.impl;

import com.example.dto.base.ApiResponse;
import com.example.dto.booking.*;
import com.example.entity.home_pages.BookingEntity;
import com.example.enums.BookingStatus;
import com.example.exp.AppBadException;
import com.example.repository.BookingRepository;
import com.example.service.service_interface.BookingService;
import com.example.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public ApiResponse<BookingResponse> createBooking( BookingRequest request) {
        Long profileId = SpringSecurityUtil.getProfileId();
        BookingEntity entity = new BookingEntity();
        entity.setProfileId(profileId);
        entity.setSalonId(request.getSalonId());
        entity.setServiceId(request.getServiceId());
        entity.setMasterId(request.getMasterId());
        entity.setStartTime(request.getStartTime());
        entity.setEndTime(request.getEndTime());
        entity.setSpecialRequests(request.getSpecialRequests());
        entity.setStatus(BookingStatus.CONFIRMED);

        BookingEntity saved = bookingRepository.save(entity);

        BookingResponse resp = new BookingResponse();
        resp.setId(saved.getId());
        resp.setSalonId(saved.getSalonId());
        resp.setServiceId(saved.getServiceId());
        resp.setMasterId(saved.getMasterId());
        resp.setStatus(saved.getStatus().name());
        resp.setStartTime(saved.getStartTime().toString());
        resp.setEndTime(saved.getEndTime().toString());
        resp.setSpecialRequests(saved.getSpecialRequests());
        resp.setPaidAmount(saved.getPaidAmount());
        resp.setPaymentMethod(saved.getPaymentMethod());

        return new ApiResponse<>(true,resp, "Booking created successfully!");
    }

    @Override
    public ApiResponse<Page<BookingDto>> getUserBookings(int page, int size) {
        Long profileId = SpringSecurityUtil.getProfileId();
        Page<BookingEntity> entityPage = bookingRepository.findByProfileId(profileId, PageRequest.of(page, size));
        Page<BookingDto> dtoPage = entityPage.map(entity -> {
            BookingDto dto = new BookingDto();
            dto.setId(entity.getId());
            dto.setSalonId(entity.getSalonId());
            dto.setServiceId(entity.getServiceId());
            dto.setMasterId(entity.getMasterId());
            dto.setStatus(entity.getStatus().name());
            dto.setStartTime(entity.getStartTime().toString());
            dto.setEndTime(entity.getEndTime().toString());
            return dto;
        });
        return ApiResponse.success(dtoPage);
    }

    @Override
    public ApiResponse<Boolean> cancelBooking( Long bookingId) {
        Long profileId = SpringSecurityUtil.getProfileId();
        BookingEntity entity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppBadException("Booking not found"));
        if (!entity.getProfileId().equals(profileId)) {
            return new ApiResponse<>(false, "Not allowed!");
        }
        entity.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(entity);
        return new ApiResponse<>(true, "Booking cancelled!");
    }

    @Override
    public ApiResponse<List<TimeSlotDto>> getAvailableSlots(Long salonId, Long serviceId, Long masterId, LocalDate date) {
        List<TimeSlotDto> slots = List.of(
                new TimeSlotDto("09:00", "10:00"),
                new TimeSlotDto("10:00", "11:00"),
                new TimeSlotDto("11:00", "12:00")
        );
        return ApiResponse.success(slots);
    }
}