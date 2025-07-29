package com.example.dto.salon;

import lombok.Data;

@Data
public class MasterDto {
    private Long id;
    private String name;
    private String specialization;
    private Double rating;
    private String photoUrl;
    private Integer experienceYears;
}