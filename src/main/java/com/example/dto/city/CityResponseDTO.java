package com.example.dto.city;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CityResponseDTO {
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private LocalDateTime createdDate;
}
