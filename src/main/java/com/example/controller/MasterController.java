package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.master.MasterCreateDto;
import com.example.dto.master.MasterResponseDto;
import com.example.dto.master.MasterUpdateDto;
import com.example.enums.LanguageEnum;
import com.example.service.MasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/masters")
@RequiredArgsConstructor
public class MasterController {

    private final MasterService masterService;

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    @PostMapping
    public ResponseEntity<ApiResult<MasterResponseDto>> create(
            @RequestBody MasterCreateDto dto,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum lang) {
        ApiResult<MasterResponseDto> response = masterService.create(dto, lang);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<MasterResponseDto>> update(
            @PathVariable Long id,
            @RequestBody MasterUpdateDto dto,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum lang) {
        ApiResult<MasterResponseDto> response = masterService.update(id, dto,lang);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Boolean>> delete(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum lang) {
        ApiResult<Boolean> result = masterService.delete(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER', 'USER', 'MASTER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<MasterResponseDto>> getById(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum lang) {
        ApiResult<MasterResponseDto> response = masterService.getById(id,lang);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER', 'USER', 'MASTER')")
    @GetMapping
    public ResponseEntity<ApiResult<List<MasterResponseDto>>> getAll() {
        ApiResult<List<MasterResponseDto>> response = masterService.getAll();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER', 'USER', 'MASTER')")
    @GetMapping("/salon/{salonId}")
    public ResponseEntity<ApiResult<List<MasterResponseDto>>> getBySalon(
            @PathVariable Long salonId,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum lang) {
        ApiResult<List<MasterResponseDto>> response = masterService.getBySalonId(salonId, lang);
        return ResponseEntity.ok(response);
    }
}