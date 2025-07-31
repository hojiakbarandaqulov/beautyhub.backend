package com.example.service.impl;

import com.example.dto.base.ApiResponse;
import com.example.dto.base.ApiResult;
import com.example.dto.booking.*;
import com.example.entity.home_pages.BookingEntity;
import com.example.enums.BookingStatus;
import com.example.enums.LanguageEnum;
import com.example.exp.AppBadException;
import com.example.repository.BookingRepository;
import com.example.service.ResourceBundleService;
import com.example.service.service_interface.BookingService;
import com.example.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ResourceBundleService messageService;

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
        return ApiResponse.success(resp);
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
    public ApiResult<String> cancelBooking(Long bookingId, LanguageEnum language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        BookingEntity entity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("booking.not.found",language)));
        if (!entity.getProfileId().equals(profileId)) {
           throw new AppBadException(messageService.getMessage("profile.not_found",language));
        }
        entity.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(entity);

        Map<LanguageEnum, String> messages = new HashMap<>();
        messages.put(LanguageEnum.uz, messageService.getMessage("booking.cancelled", LanguageEnum.uz));
        messages.put(LanguageEnum.ru, messageService.getMessage("booking.cancelled", LanguageEnum.ru));
        messages.put(LanguageEnum.en, messageService.getMessage("booking.cancelled", LanguageEnum.en));
        ApiResult<String> response = new ApiResult<String>("Success",messages);
        return new ApiResult<String>(response.getData());


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