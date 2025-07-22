package com.example.mapper;

import com.example.dto.city.CityResponseDTO;
import com.example.entity.CityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityResponseDTO toResponse(CityEntity entity);
}
