package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.salon.*;
import com.example.service.salon.SalonService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ApiResult<SalonCreateResponseDto>> create(@RequestBody SalonCreateDto dto) {
        ApiResult<SalonCreateResponseDto> result = salonService.create(dto);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResult<SalonCreateResponseDto>> update(
            @PathVariable Long id,
            @RequestBody SalonUpdateDto dto) {
        ApiResult<SalonCreateResponseDto> result = salonService.update(id, dto);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResult<Boolean>> delete(@PathVariable Long id) {
        ApiResult<Boolean> delete = salonService.delete(id);
        return ResponseEntity.ok(delete);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResult<SalonCreateResponseDto>> getById(@PathVariable Long id) {
        ApiResult<SalonCreateResponseDto> result = salonService.getById(id);
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
            @RequestParam(defaultValue = "5") Double radiusKm) {
        ApiResult<List<SalonListDto>> result = salonService.findNearby(latitude, longitude, radiusKm);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/search")
    public ResponseEntity<ApiResult<List<SalonListDto>>> search(
            @RequestParam String query,
            @RequestParam(required = false) Long category) {
        ApiResult<List<SalonListDto>> result = salonService.search(query, category);
        return ResponseEntity.ok(result);
    }
}