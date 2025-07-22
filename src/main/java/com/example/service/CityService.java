package com.example.service;

import com.example.dto.base.ApiResult;
import com.example.dto.city.CityCreateDTO;
import com.example.dto.city.CityResponseAllDTO;
import com.example.dto.city.CityResponseDTO;
import com.example.dto.city.DistrictDTO;
import com.example.entity.CityEntity;
import com.example.entity.DistrictEntity;
import com.example.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

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
}
