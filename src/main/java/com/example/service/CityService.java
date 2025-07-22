package com.example.service;

import com.example.dto.base.ApiResult;
import com.example.dto.city.*;
import com.example.entity.CityEntity;
import com.example.entity.DistrictEntity;
import com.example.repository.CityRepository;
import com.example.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;


    public ApiResult<CityResponseDTO> createCity(CityCreateDTO dto) {
        CityEntity city = new CityEntity();
        city.setNameUz(dto.getNameUz());
        city.setNameEn(dto.getNameEn());
        city.setNameRu(dto.getNameRu());
        city.setOrderNumber(dto.getOrderNumber());
        city.setCreatedDate(LocalDateTime.now());

        List<DistrictEntity> districts = dto.getDistricts().stream()
                .map(districtDTO -> {
                    DistrictEntity district = new DistrictEntity();
                    district.setNameUz(districtDTO.getNameUz());
                    district.setNameRu(districtDTO.getNameRu());
                    district.setNameEn(districtDTO.getNameEn());
                    district.setCity(city);
                    return district;
                })
                .collect(Collectors.toList());

        city.setDistricts(districts);

        CityEntity savedCity = cityRepository.save(city);

        return ApiResult.successResponse(mapToCityResponseDTO(savedCity));
    }

    public ApiResult<List<CityResponseAllDTO>> getAll() {
        Iterable<CityEntity> entity = cityRepository.findAll();
        List<CityResponseAllDTO> dtoList = new LinkedList<>();
        for (CityEntity city : entity) {
            dtoList.add(mapToCityDTO(city));
        }
        return ApiResult.successResponse(dtoList);
    }

    private CityResponseAllDTO mapToCityDTO(CityEntity city) {
        CityResponseAllDTO response = new CityResponseAllDTO();
        response.setNameUz(city.getNameUz());
        response.setNameEn(city.getNameEn());
        response.setNameRu(city.getNameRu());

        List<DistrictDTO> districtDTOs = city.getDistricts().stream()
                .map(this::mapToDistrictResponseDTO)
                .collect(Collectors.toList());

        response.setDistricts(districtDTOs);
        return response;
    }

    private CityResponseDTO mapToCityResponseDTO(CityEntity city) {
        CityResponseDTO response = new CityResponseDTO();
        response.setId(city.getId());
        response.setNameUz(city.getNameUz());
        response.setNameEn(city.getNameEn());
        response.setNameRu(city.getNameRu());

        List<DistrictDTO> districtDTOs = city.getDistricts().stream()
                .map(this::mapToDistrictResponseDTO)
                .collect(Collectors.toList());

        response.setDistricts(districtDTOs);
        return response;
    }

    private DistrictDTO mapToDistrictResponseDTO(DistrictEntity district) {
        DistrictDTO dto = new DistrictDTO();
        dto.setNameUz(district.getNameUz());
        dto.setNameRu(district.getNameRu());
        dto.setNameEn(district.getNameEn());
        return dto;
    }

    public ApiResult<List<CityResponseDTO>> citySearch(String query, String language) {
        String normalizedQuery = query.trim().toLowerCase();

        // 1. Shaharlarni qidirish (barcha mos keluvchilar)
        List<CityResponseDTO> cities = cityRepository.findAll().stream()
                .filter(city -> matchesLanguage(city, normalizedQuery, language))
                .sorted(Comparator.comparing(city -> calculateRelevance(city, normalizedQuery, language)))
                .map(city -> convertToCityResponseDTO(city)) // Qo'lda mapping
                .collect(Collectors.toList());

        List<DistrictResponseDTO> districts = districtRepository.findAll().stream()
                .filter(district -> matchesLanguage(district, normalizedQuery, language))
                .sorted(Comparator.comparing(district -> calculateRelevance(district, normalizedQuery, language)))
                .map(this::convertToDistrictResponseDTO)
                .collect(Collectors.toList());

        return new ApiResult<>(new SearchResultDTO(cities, districts), true, "search.results.retrieved");
    }

    // Qo'lda yozilgan mapping metodi
    private CityResponseDTO convertToCityResponseDTO(CityEntity city) {
        CityResponseDTO dto = new CityResponseDTO();

        // Asosiy maydonlarni map qilish
        dto.setId(city.getId());
        dto.setNameUz(city.getNameUz());
        dto.setNameRu(city.getNameRu());
        dto.setNameEn(city.getNameEn());

        return dto;
    }

    private DistrictResponseDTO convertToDistrictResponseDTO(DistrictEntity district) {
        if (district == null) return null;

        DistrictResponseDTO dto = new DistrictResponseDTO();
        dto.setId(district.getId());
        dto.setNameUz(district.getNameUz());
        dto.setNameRu(district.getNameRu());
        dto.setNameEn(district.getNameEn());
        return dto;
    }

    // Tilga qarab moslikni tekshirish
    private boolean matchesLanguage(CityEntity city, String query, String language) {
        String nameToCheck = switch (language.toLowerCase()) {
            case "ru" -> city.getNameRu().toLowerCase();
            case "en" -> city.getNameEn().toLowerCase();
            default -> city.getNameUz().toLowerCase();
        };
        return nameToCheck.contains(query);
    }


    private boolean matchesLanguage(DistrictEntity district, String query, String language) {
        String name = switch (language.toLowerCase()) {
            case "ru" -> district.getNameRu().toLowerCase();
            case "en" -> district.getNameEn().toLowerCase();
            default -> district.getNameUz().toLowerCase();
        };
        return name.contains(query);
    }

    private int calculateRelevance(CityEntity city, String query, String language) {
        String name = switch (language.toLowerCase()) {
            case "ru" -> city.getNameRu().toLowerCase();
            case "en" -> city.getNameEn().toLowerCase();
            default -> city.getNameUz().toLowerCase();
        };

        if (name.startsWith(query)) return 0;
        if (name.contains(query)) return 1;
        return 2+name.length();
    }

    private int calculateRelevance(DistrictEntity city, String query, String language) {
        String name = switch (language.toLowerCase()) {
            case "ru" -> city.getNameRu().toLowerCase();
            case "en" -> city.getNameEn().toLowerCase();
            default -> city.getNameUz().toLowerCase();
        };

        if (name.startsWith(query)) return 0;
        if (name.contains(query)) return 1;
        return 2+name.length();
    }

}

