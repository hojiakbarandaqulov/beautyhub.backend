package com.example.service.impl;

import com.example.dto.base.ApiResult;
import com.example.dto.service.ServiceCreateRequest;
import com.example.dto.service.ServiceResponse;
import com.example.dto.service.ServiceUpdateRequest;
import com.example.entity.home_pages.ServiceEntity;
import com.example.enums.LanguageEnum;
import com.example.exp.AppBadException;
import com.example.mapper.ServiceMapper;
import com.example.repository.ServiceRepository;
import com.example.service.ResourceBundleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;
    private final ModelMapper modelMapper;
    private final ResourceBundleService messageService;


    public ApiResult<ServiceResponse> create(ServiceCreateRequest request, LanguageEnum language) {
        ServiceEntity service = new ServiceEntity();
        service.setName(request.getName());
        service.setSalonId(request.getSalonId());
        service.setDescription(request.getDescription());
        service.setDuration(request.getDurationMinutes());
        service.setPrice(request.getPrice());
        ServiceEntity saved = serviceRepository.save(service);
        ServiceResponse response=modelMapper.map(saved, ServiceResponse.class);
        return ApiResult.successResponse(response);
    }

    public ApiResult<ServiceResponse> getById(Long id, LanguageEnum language) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("Service not found", language)));
        return ApiResult.successResponse(serviceMapper.toDto(service));
    }

    public ApiResult<List<ServiceResponse>> getBySalonId(Long salonId, LanguageEnum language) {
        List<ServiceEntity> services = serviceRepository.findBySalonId(salonId);
        if (services.isEmpty()) {
            throw new AppBadException(messageService.getMessage("Services not found", language));
        }
        List<ServiceResponse> dtos = services.stream()
                .map(serviceMapper::toDto)
                .toList();
        return ApiResult.successResponse(dtos);
    }

    public ApiResult<ServiceResponse> update(Long id, ServiceUpdateRequest request, LanguageEnum language) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("service.not.found", language)));

        serviceMapper.updateFromDto(request, service);
        ServiceEntity updated = serviceRepository.save(service);

        return ApiResult.successResponse(serviceMapper.toDto(updated));
    }

    public ApiResult<String> delete(Long id, LanguageEnum language) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("Service not found", language)));
        serviceRepository.delete(service);
        return ApiResult.successResponse(messageService.getMessage("Service successfully deleted", language));
    }
}

