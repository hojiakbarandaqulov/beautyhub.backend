package com.example.mapper;

import com.example.dto.service.ServiceCreateRequest;
import com.example.dto.service.ServiceResponse;
import com.example.dto.service.ServiceUpdateRequest;
import com.example.entity.home_pages.ServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceEntity toEntity(ServiceCreateRequest request);
    ServiceResponse toDto(ServiceEntity entity);
    void updateFromDto(ServiceUpdateRequest request, @MappingTarget ServiceEntity entity);
}