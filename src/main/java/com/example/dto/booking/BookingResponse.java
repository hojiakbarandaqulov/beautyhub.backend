package com.example.dto.booking;

import com.example.enums.BookingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponse {
    private Long id;
    private Long salonId;
    private Long serviceId;
    private Long masterId;
    private String status;
    private String startTime;
    private String endTime;
    private String specialRequests;
    private Double paidAmount;
    private String paymentMethod;
}
