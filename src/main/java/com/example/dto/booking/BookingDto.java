package com.example.dto.booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDto {
    @NotNull(message = "id required")
    private Long id;

    @NotBlank(message = "salonName required")
    private String salonName;

    @NotBlank(message = "serviceName required")
    private String serviceName;

    @NotBlank(message = "masterName required")
    private String masterName;

    @NotNull(message = "bookingTime required")
    private LocalDateTime bookingTime;

    @NotBlank(message = "status required")
    private String status;

    @NotNull(message = "price required")
    private Double price;
}
