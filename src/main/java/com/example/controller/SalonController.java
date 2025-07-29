package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.salon.*;
import com.example.service.service_interface.SalonService;
import com.example.enums.LanguageEnum;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/salons")
public class SalonController {

    private final SalonService salonService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<SalonCreateResponseDto>> create(@RequestBody SalonCreateDto dto,
                                                                    @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<SalonCreateResponseDto> result = salonService.create(dto,language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResult<SalonCreateResponseDto>> update(
            @PathVariable Long id,
            @RequestBody SalonUpdateDto dto,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<SalonCreateResponseDto> result = salonService.update(id, dto,language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResult<Boolean>> delete(@PathVariable Long id,
                                                     @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<Boolean> delete = salonService.delete(id,language);
        return ResponseEntity.ok(delete);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResult<SalonCreateResponseDto>> getById(@PathVariable Long id,
                                                                     @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<SalonCreateResponseDto> result = salonService.getById(id,language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getAll")
    public ResponseEntity<ApiResult<List<SalonListDto>>> getAll() {
        ApiResult<List<SalonListDto>> result = salonService.getAll();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/nearby")
    public ResponseEntity<ApiResult<List<SalonListDto>>> findNearby(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5") Double radiusKm,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<List<SalonListDto>> result = salonService.findNearby(latitude, longitude, radiusKm,language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/search")
    public ResponseEntity<ApiResult<List<SalonListDto>>> search(
            @RequestParam String query,
            @RequestParam(required = false) Long category,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<List<SalonListDto>> result = salonService.search(query, category,language);
        return ResponseEntity.ok(result);
    }
}