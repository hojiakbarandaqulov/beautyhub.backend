package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.master.MasterCreateDto;
import com.example.dto.master.MasterResponseDto;
import com.example.dto.master.MasterUpdateDto;
import com.example.enums.LanguageEnum;
import com.example.service.MasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/masters")
@RequiredArgsConstructor
@Tag(name = "Usta (Master)", description = "Ustalar bilan ishlash uchun API: yaratish, tahrirlash, o'chirish, ko'rish")
public class MasterController {

    private final MasterService masterService;

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    @PostMapping
    @Operation(
            summary = "Yangi usta yaratish",
            description = "Yangi usta (master) ma'lumotlarini kiritib tizimga qo'shadi. Faqat admin va salon managerlar foydalanadi."
    )
    public ResponseEntity<ApiResult<MasterResponseDto>> create(
            @RequestBody @Valid MasterCreateDto dto,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum lang) {
        ApiResult<MasterResponseDto> response = masterService.create(dto, lang);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    @PutMapping("/update/{id}")
    @Operation(
            summary = "Usta ma'lumotlarini tahrirlash",
            description = "Berilgan id bo'yicha ustani tahrirlaydi. Faqat admin va salon managerlar foydalanadi."
    )
    public ResponseEntity<ApiResult<MasterResponseDto>> update(
            @PathVariable Long id,
            @RequestBody @Valid MasterUpdateDto dto,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum lang) {
        ApiResult<MasterResponseDto> response = masterService.update(id, dto, lang);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Ustani o'chirish",
            description = "Berilgan id bo'yicha ustani tizimdan o'chiradi. Faqat admin va salon managerlar uchun."
    )
    public ResponseEntity<ApiResult<Boolean>> delete(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum lang) {
        ApiResult<Boolean> result = masterService.delete(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER', 'USER', 'MASTER')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Ustani ko'rish",
            description = "Berilgan id bo'yicha ustani to'liq ma'lumotlarini qaytaradi. Barcha role uchun."
    )
    public ResponseEntity<ApiResult<MasterResponseDto>> getById(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum lang) {
        ApiResult<MasterResponseDto> response = masterService.getById(id, lang);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER', 'USER', 'MASTER')")
    @GetMapping("/getAll")
    @Operation(
            summary = "Barcha ustalar ro'yxati",
            description = "Tizimdagi barcha ustalar ro'yxatini qaytaradi. Barcha role uchun."
    )
    public ResponseEntity<ApiResult<List<MasterResponseDto>>> getAll() {
        ApiResult<List<MasterResponseDto>> response = masterService.getAll();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER', 'USER', 'MASTER')")
    @GetMapping("/salon/{salonId}")
    @Operation(
            summary = "Salon ustalari ro'yxati",
            description = "Berilgan salonId bo'yicha shu salonda ishlaydigan ustalar ro'yxatini qaytaradi. Barcha role uchun."
    )
    public ResponseEntity<ApiResult<List<MasterResponseDto>>> getBySalon(
            @PathVariable Long salonId,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum lang) {
        ApiResult<List<MasterResponseDto>> response = masterService.getBySalonId(salonId, lang);
        return ResponseEntity.ok(response);
    }
}