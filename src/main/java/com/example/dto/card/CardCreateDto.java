package com.example.dto.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CardCreateDto {

    @NotBlank
    @Pattern(regexp = "\\d{16}", message = "Card number must be exactly 16 digits")
    private String number;

    @NotBlank
    private String holder;

    private Double balance;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\+998\\d{9}", message = "Phone number must be in Uzbekistan format")
    private String phoneNumber;
}
