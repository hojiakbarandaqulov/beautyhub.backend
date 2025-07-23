package com.example.dto.city;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DistrictResponseDTO {
    private Long id;
    private String nameUz;
    private String nameRu;
    private String nameEn;
}
