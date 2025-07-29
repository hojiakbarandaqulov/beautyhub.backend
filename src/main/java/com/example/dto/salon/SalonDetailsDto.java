package com.example.dto.salon;

import lombok.Data;

import java.util.List;

@Data
public class SalonDetailsDto {
    private Long id;

    private String name;
    private String address;
    private String phone;
    private String description;
    private Double rating;
    private List<String> imageUrls;
  /*  private List<ServiceDto> services;
    private List<MasterDto> masters;
    private List<ReviewDto> reviews;*/
}