package com.example.service;

import com.example.dto.base.ApiResult;
import com.example.dto.city.CityCreateDTO;
import com.example.dto.city.CityResponseDTO;
import com.example.entity.CityEntity;
import com.example.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public ApiResult<CityResponseDTO> create(CityCreateDTO city) {
        CityEntity entity = new CityEntity();
        entity.setOrderNumber(city.getOrderNumber());
        entity.setNameUz(city.getNameUz());
        entity.setNameRu(city.getNameRu());
        entity.setNameEn(city.getNameEn());
        cityRepository.save(entity);
        return ApiResult.successResponse(regionToDTO(entity));
    }

    public ApiResult<List<CityResponseDTO>> getAll() {
        Iterable<CityEntity> entity = cityRepository.findAll();
        List<CityResponseDTO> dtoList = new LinkedList<>();
        for (CityEntity region : entity) {
            dtoList.add(regionToDTO(region));
        }
        return ApiResult.successResponse(dtoList);
    }
    private CityResponseDTO regionToDTO(CityEntity entity){
        CityResponseDTO city = new CityResponseDTO();
        city.setNameUz(entity.getNameUz());
        city.setNameRu(entity.getNameRu());
        city.setNameEn(entity.getNameEn());
        city.setCreatedDate(entity.getCreatedDate());
        return city;
    }
}
