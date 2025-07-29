package com.example.dto.booking;

import com.example.enums.BookingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
    @NotNull(message = "id required")
    private Long id;

    @NotBlank(message = "salonName required")
    private String salonName;

    @NotBlank(message = "serviceName required")
    private String serviceName;

    @NotBlank(message = "masterName required")
    private String masterName;

    @NotNull(message = "startTime required")
    private LocalDateTime startTime;

    @NotNull(message = "endTime required")
    private LocalDateTime endTime;

    @NotNull(message = "price required")
    private Double price;

    @NotNull(message = "earnedPoints required")
    private Integer earnedPoints;

    @NotNull(message = "status required")
    private BookingStatus status;

    @NotBlank(message = "qrCodeUrl required")
    private String qrCodeUrl;

    @NotNull(message = "paymentRequired required")
    private Boolean paymentRequired;

    @NotNull(message = "paymentAmount required")
    private Double paymentAmount;
}
