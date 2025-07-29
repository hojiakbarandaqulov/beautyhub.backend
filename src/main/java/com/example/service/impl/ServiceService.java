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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    public ApiResult<ServiceResponse> create(ServiceCreateRequest request, LanguageEnum language) {
        ServiceEntity entity = serviceMapper.toEntity(request);
        ServiceEntity saved = serviceRepository.save(entity);
        return ApiResult.successResponse(serviceMapper.toDto(saved));
    }

    public ApiResult<ServiceResponse> getById(Long id, LanguageEnum language) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new AppBadException(getMessage("Service not found", language)));
        return ApiResult.successResponse(serviceMapper.toDto(service));
    }

    public ApiResult<List<ServiceResponse>> getBySalonId(Long salonId, LanguageEnum language) {
        List<ServiceEntity> services = serviceRepository.findBySalonId(salonId);
        if (services.isEmpty()) {
            throw new AppBadException(getMessage("Services not found", language));
        }
        List<ServiceResponse> dtos = services.stream()
                .map(serviceMapper::toDto)
                .toList();
        return ApiResult.successResponse(dtos);
    }

    public ApiResult<ServiceResponse> update(Long id, ServiceUpdateRequest request, LanguageEnum language) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new AppBadException(getMessage("Service not found", language)));

        serviceMapper.updateFromDto(request, service);
        ServiceEntity updated = serviceRepository.save(service);

        return ApiResult.successResponse(serviceMapper.toDto(updated));
    }

    public ApiResult<String> delete(Long id, LanguageEnum language) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new AppBadException(getMessage("Service not found", language)));
        serviceRepository.delete(service);
        return ApiResult.successResponse(getMessage("Service successfully deleted", language));
    }

    private String getMessage(String defaultMessage, LanguageEnum language) {
        return switch (language) {
            case uz -> defaultMessage.equals("Service not found") ? "Xizmat topilmadi" :
                    defaultMessage.equals("Service successfully deleted") ? "Xizmat o‘chirildi" :
                            defaultMessage;
            case en -> defaultMessage; // default English
            case ru -> defaultMessage.equals("Service not found") ? "Услуга не найдена" :
                    defaultMessage.equals("Service successfully deleted") ? "Услуга удалена" :
                            defaultMessage;
        };
    }
}

