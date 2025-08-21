package com.example.service.service_interface;

import com.example.dto.base.ApiResult;
import com.example.dto.salon.*;
import com.example.enums.LanguageEnum;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface SalonService {
    ApiResult<SalonListDto> getById(Long id,LanguageEnum languageEnum);

    ApiResult<List<SalonListDto>> getAll();

    ApiResult<PageImpl<SalonListDto>> getSalons(int page, int size);

    ApiResult<List<SalonListDto>> findNearby(Double latitude, Double longitude, Double radiusKm,LanguageEnum languageEnum);

    ApiResult<List<SalonListDto>> search(String query, Long category,int page,int size,LanguageEnum languageEnum);
}
