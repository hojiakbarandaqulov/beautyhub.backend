package com.example.service.salon;

import com.example.dto.base.ApiResult;
import com.example.dto.salon.*;

import java.util.List;

public interface SalonService {
    ApiResult<SalonCreateResponseDto> create(SalonCreateDto dto);

    ApiResult<SalonCreateResponseDto> update(Long id, SalonUpdateDto dto);

    ApiResult<Boolean> delete(Long id);

    ApiResult<SalonCreateResponseDto> getById(Long id);

    ApiResult<List<SalonListDto>> getAll();

    ApiResult<List<SalonListDto>> findNearby(Double latitude, Double longitude, Double radiusKm);

    ApiResult<List<SalonListDto>> search(String query, Long category);
}
