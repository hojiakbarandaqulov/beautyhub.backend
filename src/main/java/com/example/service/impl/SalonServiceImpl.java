package com.example.service.impl;

import com.example.dto.base.ApiResult;
import com.example.dto.salon.*;
import com.example.entity.home_pages.Salon;
import com.example.exp.AppBadException;
import com.example.repository.SalonRepository;
import com.example.service.salon.SalonService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;
    private final ModelMapper modelMapper;

    public SalonServiceImpl(SalonRepository salonRepository, ModelMapper modelMapper) {
        this.salonRepository = salonRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResult<SalonCreateResponseDto>create(SalonCreateDto dto) {
        Salon salon = modelMapper.map(dto, Salon.class);
        salon.setRating(0.0);
        Salon savedSalon = salonRepository.save(salon);
        SalonCreateResponseDto map = modelMapper.map(savedSalon, SalonCreateResponseDto.class);
        return new ApiResult<>(map);
    }

    @Override
    public ApiResult<SalonCreateResponseDto> update(Long id, SalonUpdateDto dto) {
        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Salon topilmadi"));

        modelMapper.map(dto, salon);
        Salon updatedSalon = salonRepository.save(salon);
        SalonCreateResponseDto map = modelMapper.map(updatedSalon, SalonCreateResponseDto.class);
        return new ApiResult<>(map);
    }

    @Override
    public ApiResult<Boolean> delete(Long id) {
        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Salon topilmadi"));
        salonRepository.delete(salon);
        return ApiResult.successResponse(true);
    }

    @Override
    public ApiResult<SalonCreateResponseDto> getById(Long id) {
        Salon salon =salonRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Salon topilmadi"));
        SalonCreateResponseDto map = modelMapper.map(salon, SalonCreateResponseDto.class);
        return new ApiResult<>(map);
    }

    @Override
    public ApiResult<List<SalonListDto>> getAll() {
        List<Salon> salons = salonRepository.findAll();

        List<SalonListDto> salonListDtos = salons.stream()
                .map(salon -> modelMapper.map(salon, SalonListDto.class))
                .collect(Collectors.toList());

        return  ApiResult.successResponse(salonListDtos);
    }

    @Override
    public ApiResult<List<SalonListDto>> findNearby(Double latitude, Double longitude, Double radiusKm) {
         salonRepository.findNearbySalons(latitude, longitude, radiusKm).stream()
                .map(salon -> {
                    SalonListDto dto = modelMapper.map(salon, SalonListDto.class);
                    return new ApiResult<>(dto);
                })
                .collect(Collectors.toList());
         return null;
    }

    @Override
    public ApiResult<List<SalonListDto>> search(String query, Long category) {
        List<Salon> salons;
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