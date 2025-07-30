package com.example.service;


import com.example.dto.base.ApiResult;
import com.example.dto.master.*;
import com.example.entity.home_pages.Master;
import com.example.entity.home_pages.SalonEntity;
import com.example.entity.home_pages.ServiceEntity;
import com.example.enums.LanguageEnum;
import com.example.exp.AppBadException;
import com.example.repository.MasterRepository;
import com.example.repository.SalonRepository;
import com.example.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MasterService {

    private final MasterRepository masterRepository;
    private final SalonRepository salonRepository;
    private final ServiceRepository serviceRepository;
    private final ResourceBundleService messageService;

    public ApiResult<MasterResponseDto> create(MasterCreateDto dto, LanguageEnum languageEnum) {
        Master master = new Master();
        master.setName(dto.getName());
        master.setSpecialization(dto.getSpecialization());
        master.setRating(dto.getRating());
        master.setPhoto(dto.getPhotoId());

        SalonEntity salon = salonRepository.findById(dto.getSalonId())
                .orElseThrow(() -> new AppBadException(messageService.getMessage("salon.not.found", languageEnum)));
        master.setSalon(salon);

        if (dto.getServiceId() != null) {
            Set<ServiceEntity> services = new HashSet<>(serviceRepository.findAllById(dto.getServiceId()));
            master.setServices(services);
        }

        Master saved = masterRepository.save(master);
        return ApiResult.successResponse(toDto(saved));
    }

    public ApiResult<MasterResponseDto> update(Long id, MasterUpdateDto dto, LanguageEnum language) {
        Master master = masterRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("master.not.found", language)));

        master.setName(dto.getName());
        master.setSpecialization(dto.getSpecialization());
        master.setRating(dto.getRating());
        master.setPhoto(dto.getPhotoId());

        if (dto.getServiceId() != null) {
            Set<ServiceEntity> services = new HashSet<>(serviceRepository.findAllById(dto.getServiceId()));
            master.setServices(services);
        }

        Master updated = masterRepository.save(master);
        return ApiResult.successResponse(toDto(updated));
    }

    public ApiResult<Boolean> delete(Long id) {
        if (!masterRepository.existsById(id)) return ApiResult.successResponse(Boolean.FALSE);
        masterRepository.deleteById(id);
        return ApiResult.successResponse(Boolean.TRUE);
    }

    public ApiResult<MasterResponseDto> getById(Long id, LanguageEnum language) {
        Master master = masterRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("master.not.found",language)));
        return ApiResult.successResponse(toDto(master));
    }

    public ApiResult<List<MasterResponseDto>> getAll() {
        List<MasterResponseDto> masters = masterRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ApiResult.successResponse(masters);
    }


    public ApiResult<List<MasterResponseDto>> getBySalonId(Long salonId, LanguageEnum language) {
        List<Master> masterList = masterRepository.findBySalonId(salonId);
        if (masterList.isEmpty()) {
            throw new AppBadException(messageService.getMessage("salon.not.found", language));
        }

        List<MasterResponseDto> dtoList = new ArrayList<>();
        for (Master master : masterList) {
            MasterResponseDto dto = toDto(master);
            dtoList.add(dto);
        }

        return ApiResult.successResponse(dtoList);
    }

    private MasterResponseDto toDto(Master master) {
        MasterResponseDto dto = new MasterResponseDto();
        dto.setId(master.getId());
        dto.setName(master.getName());
        dto.setSpecialization(master.getSpecialization());
        dto.setRating(master.getRating());
        dto.setPhotoId(master.getPhoto());
        dto.setSalonId(master.getSalon() != null ? master.getSalon().getId() : null);
        dto.setServiceIds(
                master.getServices() != null
                        ? master.getServices().stream().map(ServiceEntity::getId).collect(Collectors.toSet())
                        : Collections.emptySet()
        );
        return dto;
    }
}