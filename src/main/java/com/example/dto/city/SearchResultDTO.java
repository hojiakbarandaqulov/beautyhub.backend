package com.example.dto.city;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResultDTO {
    private List<CityResponseDTO> cities;
    private List<DistrictResponseDTO> districts;
}