package com.example.controller;

import com.example.dto.base.ApiResponse;
import com.example.dto.base.ApiResult;
import com.example.dto.booking.*;
import com.example.enums.LanguageEnum;
import com.example.service.service_interface.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@AllArgsConstructor
@Tag(name = "Booking API", description = "Bron qilish va boshqarish uchun API")
public class BookingController {

    private final BookingService bookingService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    @Operation(summary = "Yangi bron yaratish")
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/bron")
    @Operation(summary = "Foydalanuvchi bronlari")
    public ResponseEntity<ApiResponse<Page<BookingDto>>> getUserBookings(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookingService.getUserBookings(page - 1, size));
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/cancel/{id}")
    @Operation(summary = "Bronni bekor qilish")
    public ResponseEntity<ApiResult<String>> cancelBooking(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language") LanguageEnum language) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, language));
    }

    @PreAuthorize("hasAnyRole('USER', 'SALON_MANAGER', 'MASTER')")
    @GetMapping("/available-slots")
    @Operation(summary = "Bo'sh vaqtlarni olish")
    public ResponseEntity<ApiResponse<List<TimeSlotDto>>> getAvailableSlots(
            @RequestParam Long salonId,
            @RequestParam Long serviceId,
            @RequestParam Long masterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(bookingService.getAvailableSlots(salonId, serviceId, masterId, date));
    }
}
