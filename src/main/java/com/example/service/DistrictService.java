package com.example.service;

import com.example.dto.base.ApiResult;
import com.example.dto.city.DistrictResponseDTO;
import com.example.dto.city.DistrictResponseLanguageDTO;
import com.example.entity.DistrictEntity;
import com.example.enums.LanguageEnum;
import com.example.exp.AppBadException;
import com.example.mapper.DistrictMapper;
import com.example.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class DistrictService {
    private final DistrictRepository districtRepository;
    private final ResourceBundleService messageService;
/*
    public ApiResult<String> updateDistrict(Long id, DistrictUpdate districtUpdate,LanguageEnum language) {
        Optional<DistrictEntity> district = districtRepository.findById(id);
        if (district.isEmpty()) {
            throw new AppBadException(messageService.getMessage("district.not.found",language));
        }
        DistrictEntity entity = district.get();
        districtRepository.save(entity);
        return new ApiResult<>("District muvaffaqiyatli yangilandi");
    }*/

    public ApiResult<List<DistrictResponseLanguageDTO>> getAllByLang(Long id, LanguageEnum lang) {
        List<DistrictMapper> mapperList = districtRepository.findAllByLanguage(lang.name(),id);
        List<DistrictResponseLanguageDTO> dtoList = new LinkedList<>();
        for (DistrictMapper entity : mapperList) {
            DistrictResponseLanguageDTO dto = new DistrictResponseLanguageDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dtoList.add(dto);
        }
        return ApiResult.successResponse(dtoList);
    }
}
