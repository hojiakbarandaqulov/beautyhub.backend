package com.example.dto.salon;

import lombok.Data;

import java.util.List;

@Data
public class SalonListDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private boolean isActive;
    private boolean subscriptionActive;
    private boolean isVerified;
}
