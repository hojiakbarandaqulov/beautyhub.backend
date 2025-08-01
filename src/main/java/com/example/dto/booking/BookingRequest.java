package com.example.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {
    private Long salonId;
    private Long serviceId;
    private Long masterId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String specialRequests;
}
