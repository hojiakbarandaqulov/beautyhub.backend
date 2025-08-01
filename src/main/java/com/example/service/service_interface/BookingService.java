package com.example.service.service_interface;
import com.example.dto.base.ApiResponse;
import com.example.dto.booking.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    ApiResponse<BookingResponse> createBooking(BookingRequest request);
    ApiResponse<Page<BookingDto>> getUserBookings(int page, int size);
    ApiResponse<Boolean> cancelBooking(Long bookingId);
    ApiResponse<List<TimeSlotDto>> getAvailableSlots(Long salonId, Long serviceId, Long masterId, LocalDate date);
}
