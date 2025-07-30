package com.example.controller;

import com.example.dto.base.ApiResponse;
import com.example.dto.booking.*;
import com.example.service.service_interface.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    @Operation(summary = "Yangi bron yaratish")
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @Valid @RequestBody BookingRequest request,
            @AuthenticationPrincipal UserDetails user) {
        Long userId = Long.parseLong(user.getUsername());
        return ResponseEntity.ok(bookingService.createBooking(userId, request));
    }

    @GetMapping("/bron")
    @Operation(summary = "Foydalanuvchi bronlari")
    public ResponseEntity<ApiResponse<Page<BookingDto>>> getUserBookings(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = Long.parseLong(user.getUsername());
        return ResponseEntity.ok(bookingService.getUserBookings(userId, page, size));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Bronni bekor qilish")
    public ResponseEntity<ApiResponse<Boolean>> cancelBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {
        Long userId = Long.parseLong(user.getUsername());
        return ResponseEntity.ok(bookingService.cancelBooking(userId, id));
    }

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
