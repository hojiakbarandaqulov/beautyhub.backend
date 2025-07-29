package com.example.service.service_interface;

import com.example.dto.base.ApiResult;
import com.example.dto.salon.*;
import com.example.enums.LanguageEnum;

import java.util.List;

public interface SalonService {

    ApiResult<SalonCreateResponseDto> create(SalonCreateDto dto, LanguageEnum languageEnum);

    ApiResult<SalonCreateResponseDto> update(Long id, SalonUpdateDto dto, LanguageEnum languageEnum);

    ApiResult<Boolean> delete(Long id,LanguageEnum languageEnum);

    ApiResult<SalonCreateResponseDto> getById(Long id,LanguageEnum languageEnum);

    ApiResult<List<SalonListDto>> getAll();

    ApiResult<List<SalonListDto>> findNearby(Double latitude, Double longitude, Double radiusKm,LanguageEnum languageEnum);

    ApiResult<List<SalonListDto>> search(String query, Long category,LanguageEnum languageEnum);
}
