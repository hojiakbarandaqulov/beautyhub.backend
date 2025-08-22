package com.example.service.impl;

import com.example.dto.base.ApiResult;
import com.example.dto.salon.*;
import com.example.service.ResourceBundleService;
import com.example.service.service_interface.SalonService;
import com.example.entity.home_pages.AdminAppsSalonEntity;
import com.example.enums.LanguageEnum;
import com.example.exp.AppBadException;
import com.example.repository.SalonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;
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
    public ApiResult<SalonListDto> getById(Long id, LanguageEnum languageEnum) {
        AdminAppsSalonEntity salon =salonRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("salon.not.found",languageEnum)));
        SalonListDto map = modelMapper.map(salon, SalonListDto.class);
        return new ApiResult<>(map);
    }


    @Override
    public ApiResult<List<SalonListDto>> getAll() {
        List<AdminAppsSalonEntity> salons = salonRepository.findAll();

        List<SalonListDto> salonListDtos = salons.stream()
                .map(salon -> modelMapper.map(salon, SalonListDto.class))
                .collect(Collectors.toList());
        return  ApiResult.successResponse(salonListDtos);
    }

    @Override
    public ApiResult<PageImpl<SalonListDto>> getSalons(int page, int size) {

        return null;
    }

    @Override
    public ApiResult<List<SalonListDto>> findNearby(Double latitude, Double longitude, Double radiusKm,LanguageEnum languageEnum) {
        List<AdminAppsSalonEntity> nearbySalons = salonRepository.findNearbySalons(latitude, longitude, radiusKm);

        List<SalonListDto> salonListDtos = nearbySalons.stream()
                .map(salon -> modelMapper.map(salon, SalonListDto.class))
                .collect(Collectors.toList());

        return ApiResult.successResponse(salonListDtos);
    }

    @Override
    public ApiResult<List<SalonListDto>> search(String query, Long category, int page, int size,LanguageEnum languageEnum) {
        Pageable pageable = PageRequest.of(page, size);

        Page<AdminAppsSalonEntity> salonPage = salonRepository.searchSalonsByQueryAndCategory(query, category, pageable);

        List<SalonListDto> salonListDtos = salonPage.getContent().stream()
                .map(salon -> modelMapper.map(salon, SalonListDto.class))
                .collect(Collectors.toList());

        return ApiResult.successResponse(salonListDtos);
    }

    @Override
    public Page<SalonListDto> getOutdoor(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminAppsSalonEntity> byOutdoorTrue = salonRepository.findByIsOutdoorTrue(pageable);
        return byOutdoorTrue.map(salon->modelMapper.map(salon,SalonListDto.class));
    }

    @Override
    public Page<SalonListDto> getForKids(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminAppsSalonEntity> forKidsSalonsPage = salonRepository.findByIsForKidsTrue(pageable);
        return forKidsSalonsPage.map(salon -> modelMapper.map(salon, SalonListDto.class));
    }
}