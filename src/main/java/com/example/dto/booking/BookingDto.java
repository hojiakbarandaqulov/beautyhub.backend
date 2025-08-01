package com.example.dto.booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDto {
    private Long id;
    private Long salonId;
    private Long serviceId;
    private Long masterId;
    private String status;
    private String startTime;
    private String endTime;
}