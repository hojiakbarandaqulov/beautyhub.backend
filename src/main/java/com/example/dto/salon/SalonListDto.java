package com.example.dto.salon;

import lombok.Data;

import java.util.List;

@Data
public class SalonListDto {

    private Long id;
    private String name;
    private String address;
    private Double distance; // km
    private Double rating;
    private String mainImageUrl;
    private List<String> categories;
}
