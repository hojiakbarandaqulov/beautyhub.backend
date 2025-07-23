package com.example.dto.city;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CityResponseDTO {
    private Long id;
    @NotBlank
    private String nameUz;

    @NotBlank
    private String nameRu;

    @NotBlank
    private String nameEn;

    private List<DistrictDTO> districts;

}
