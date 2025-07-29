package com.example.dto.salon;

import lombok.Data;

@Data
public class SalonUpdateDto {

    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String description;
}