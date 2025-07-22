package com.example.dto.city;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
@Data
public class CityResponseAllDTO {
    @NotBlank
    private String nameUz;

    @NotBlank
    private String nameRu;

    @NotBlank
    private String nameEn;
    private List<DistrictDTO> districts;
}
