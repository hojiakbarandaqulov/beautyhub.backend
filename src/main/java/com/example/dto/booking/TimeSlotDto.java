package com.example.dto.booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class TimeSlotDto {
    @NotNull(message = "startTime required")
    private String startTime;

    @NotNull(message = "endTime required")
    private String endTime;

    private boolean available;

    public TimeSlotDto(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = true;
    }
}