/*
package com.example.service.impl;


import com.example.Application;
import com.example.dto.base.ApiResponse;
import com.example.dto.booking.*;
import com.example.entity.ProfileEntity;
import com.example.entity.home_pages.BookingEntity;
import com.example.entity.home_pages.Master;
import com.example.entity.home_pages.SalonEntity;
import com.example.entity.home_pages.ServiceEntity;
import com.example.enums.BookingStatus;
import com.example.exp.AppBadException;
import com.example.mapper.BookingMapper;
import com.example.repository.*;
import com.example.service.service_interface.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final SalonRepository salonRepository;
    private final ServiceRepository serviceRepository;
    private final MasterRepository masterRepository;
    private final ProfileRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public ApiResponse<BookingResponse> createBooking(Long userId, BookingRequest request) {
        // Validatsiyalar
        ProfileEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new AppBadException("Foydalanuvchi topilmadi"));
        SalonEntity salon = salonRepository.findById(request.getSalonId())
                .orElseThrow(() -> new AppBadException("Salon topilmadi"));
        ServiceEntity service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new AppBadException("Xizmat topilmadi"));
        Master master = masterRepository.findById(request.getMasterId())
                .orElseThrow(() -> new AppBadException("Usta topilmadi"));

        // Vaqtni tekshirish
        checkTimeAvailability(master, request.getStartTime(), service.getDurationMinutes());

        // Bronni yaratish
        BookingEntity booking = new BookingEntity();
        booking.setProfile(user);
        booking.setSalon(salon);
        booking.setService(service);
        booking.setMaster(master);
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getStartTime().plusMinutes(service.getDurationMinutes()));
        booking.setStatus(BookingStatus.CONFIRMED); // Avtomatik tasdiqlangan
        booking.setSpecialRequests(request.getSpecialRequests());

        BookingEntity savedBooking = bookingRepository.save(booking);
        return ApiResponse.success(bookingMapper.toResponse(savedBooking));
    }

    private void checkTimeAvailability(Master master, LocalDateTime startTime, int duration) {
        boolean isAvailable = bookingRepository
                .findByMasterAndTimeRange(master.getId(), startTime, startTime.plusMinutes(duration))
                .isEmpty();

        if (!isAvailable) {
            throw new AppBadException("Tanlangan vaqt band");
        }
    }

    @Override
    public ApiResponse<Page<BookingDto>> getUserBookings(Long userId, int page, int size) {
        Page<BookingEntity> bookings = bookingRepository.findByProfileId(userId, PageRequest.of(page, size));
        return ApiResponse.success(bookings.map(bookingMapper::toDto));
    }

    @Override
    @Transactional
    public ApiResponse<Boolean> cancelBooking(Long userId, Long bookingId) {
        BookingEntity booking = bookingRepository.findByIdAndProfileId(bookingId, userId)
                .orElseThrow(() -> new AppBadException("Bron topilmadi"));

        // Faqat kelajakdagi bronlarni bekor qilish mumkin
        if (booking.getStartTime().isBefore(LocalDateTime.now())) {
            throw new AppBadException("O'tgan bronni bekor qilib bo'lmaydi");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        return ApiResponse.success(true);
    }

    @Override
    public ApiResponse<List<TimeSlotDto>> getAvailableSlots(Long salonId, Long serviceId, Long masterId, LocalDate date) {
        // 9:00 dan 18:00 gacha bo'lgan vaqt oralig'ida bo'sh vaqtlarni hisoblash
        // Bu oddiy misol, haqiqiy loyihada salon ish vaqtlariga qarab hisoblash kerak
        return ApiResponse.success(List.of(
                new TimeSlotDto("09:00", "10:00", true),
                new TimeSlotDto("10:00", "11:00", true),
                new TimeSlotDto("11:00", "12:00", false) // band
        ));
    }
}
*/
