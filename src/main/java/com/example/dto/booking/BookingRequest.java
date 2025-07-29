package com.example.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {
    @NotNull(message = "salonId required")
    private Long salonId;

    @NotNull(message = "serviceId required")
    private Long serviceId;

    @NotNull(message = "masterId required")
    private Long masterId;

    @NotNull(message = "startTime required")
    @Future
    private LocalDateTime startTime;

    private boolean payNow;
//    private PaymentMethod paymentMethod;
    private String promoCode;
    private String specialRequests;
}
