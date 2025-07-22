package com.example.dto.city;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class CityCreateDTO {
    @NotBlank
    private String nameUz;

    @NotBlank
    private String nameRu;

    @NotBlank
    private String nameEn;

    @Valid
    @NotEmpty
    private List<DistrictDTO> districts;

    private Integer orderNumber;
}

