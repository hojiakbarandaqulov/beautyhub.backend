package com.example.service.impl;

import com.example.dto.base.ApiResult;
import com.example.dto.salon.*;
import com.example.service.ResourceBundleService;
import com.example.service.service_interface.SalonService;
import com.example.entity.home_pages.SalonEntity;
import com.example.enums.LanguageEnum;
import com.example.exp.AppBadException;
import com.example.repository.SalonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;
    private final ModelMapper modelMapper;
    private final ResourceBundleService messageService;

    public SalonServiceImpl(SalonRepository salonRepository, ModelMapper modelMapper, ResourceBundleService messageService) {
        this.salonRepository = salonRepository;
        this.modelMapper = modelMapper;
        this.messageService = messageService;
    }

    @Override
    public ApiResult<SalonCreateResponseDto>create(SalonCreateDto dto,LanguageEnum languageEnum) {
        SalonEntity salon = modelMapper.map(dto, SalonEntity.class);
        salon.setRating(0.0);
        SalonEntity savedSalon = salonRepository.save(salon);
        SalonCreateResponseDto map = modelMapper.map(savedSalon, SalonCreateResponseDto.class);
        return new ApiResult<>(map);
    }

    @Override
    public ApiResult<SalonCreateResponseDto> update(Long id, SalonUpdateDto dto,LanguageEnum languageEnum) {
        SalonEntity salon = salonRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Salon topilmadi"));

        modelMapper.map(dto, salon);
        SalonEntity updatedSalon = salonRepository.save(salon);
        SalonCreateResponseDto map = modelMapper.map(updatedSalon, SalonCreateResponseDto.class);
        return new ApiResult<>(map);
    }

    @Override
    public ApiResult<Boolean> delete(Long id, LanguageEnum languageEnum) {
        SalonEntity salon = salonRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("salon.not.found",languageEnum)));
        salonRepository.delete(salon);
        return ApiResult.successResponse(true);
    }

    @Override
    public ApiResult<SalonCreateResponseDto> getById(Long id, LanguageEnum languageEnum) {
        SalonEntity salon =salonRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("salon.not.found",languageEnum)));
        SalonCreateResponseDto map = modelMapper.map(salon, SalonCreateResponseDto.class);
        return new ApiResult<>(map);
    }


    @Override
    public ApiResult<List<SalonListDto>> getAll() {
        List<SalonEntity> salons = salonRepository.findAll();

        List<SalonListDto> salonListDtos = salons.stream()
                .map(salon -> modelMapper.map(salon, SalonListDto.class))
                .collect(Collectors.toList());

        return  ApiResult.successResponse(salonListDtos);
    }

    @Override
    public ApiResult<List<SalonListDto>> findNearby(Double latitude, Double longitude, Double radiusKm,LanguageEnum languageEnum) {
         salonRepository.findNearbySalons(latitude, longitude, radiusKm).stream()
                .map(salon -> {
                    SalonListDto dto = modelMapper.map(salon, SalonListDto.class);
                    return new ApiResult<>(dto);
                })
                .collect(Collectors.toList());
         return null;
    }

    @Override
    public ApiResult<List<SalonListDto>> search(String query, Long category, LanguageEnum languageEnum) {
        List<SalonEntity> salons;
        if (category != null) {
            salons = salonRepository.findByCategory(category);
        } else {
            salons = salonRepository.searchSalons(query);
        }

         salons.stream()
                 .map(salon -> {
                     SalonListDto dto = modelMapper.map(salon, SalonListDto.class);
                     return new ApiResult<>(dto);
                 })
                 .collect(Collectors.toList());
        return null;
    }
}