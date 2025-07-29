package com.example.service.service_interface;
import com.example.dto.base.ApiResponse;
import com.example.dto.booking.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    ApiResponse<BookingResponse> createBooking(Long userId, BookingRequest request);
    ApiResponse<Page<BookingDto>> getUserBookings(Long userId, int page, int size);
    ApiResponse<Boolean> cancelBooking(Long userId, Long bookingId);
    ApiResponse<List<TimeSlotDto>> getAvailableSlots(Long salonId, Long serviceId, Long masterId, LocalDate date);

}
