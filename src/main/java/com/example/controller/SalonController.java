package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.salon.*;
import com.example.service.service_interface.SalonService;
import com.example.enums.LanguageEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/salons")
@Tag(name = "Salon", description = "Salonlarni yaratish, tahrirlash, o'chirish, qidirish va yaqin atrofdagi salonlarni topish uchun APIlar")
public class SalonController {

    private final SalonService salonService;

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    @PostMapping("/create")
    @Operation(
            summary = "Yangi salon yaratish",
            description = "Yangi salon ma'lumotlarini tizimga qo'shadi (nomi, manzili, va h.k.). Faqat admin va salon managerlar foydalanadi."
    )
    public ResponseEntity<ApiResult<SalonCreateResponseDto>> create(@RequestBody SalonCreateDto dto,
                                                                    @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<SalonCreateResponseDto> result = salonService.create(dto, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    @PutMapping("/update/{id}")
    @Operation(
            summary = "Salon ma'lumotlarini tahrirlash",
            description = "Berilgan id bo'yicha salon ma'lumotlarini yangilaydi. Faqat admin va salon managerlar foydalanadi."
    )
    public ResponseEntity<ApiResult<SalonCreateResponseDto>> update(
            @PathVariable Long id,
            @RequestBody SalonUpdateDto dto,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<SalonCreateResponseDto> result = salonService.update(id, dto, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Salon o'chirish",
            description = "Berilgan id bo'yicha salonni tizimdan o'chiradi. Faqat admin va salon managerlar foydalanadi."
    )
    public ResponseEntity<ApiResult<Boolean>> delete(@PathVariable Long id,
                                                     @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<Boolean> delete = salonService.delete(id, language);
        return ResponseEntity.ok(delete);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MASTER', 'SALON_MANAGER')")
    @GetMapping("/get/{id}")
    @Operation(
            summary = "Salon tafsilotlari",
            description = "Berilgan id bo'yicha salonning to'liq ma'lumotlarini qaytaradi. Barcha role uchun."
    )
    public ResponseEntity<ApiResult<SalonCreateResponseDto>> getById(@PathVariable Long id,
                                                                     @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<SalonCreateResponseDto> result = salonService.getById(id, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MASTER', 'SALON_MANAGER')")
    @GetMapping("/getAll")
    @Operation(
            summary = "Barcha salonlar ro'yxati",
            description = "Tizimdagi barcha salonlar ro'yxatini qaytaradi. Barcha role uchun."
    )
    public ResponseEntity<ApiResult<List<SalonListDto>>> getAll() {
        ApiResult<List<SalonListDto>> result = salonService.getAll();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MASTER', 'SALON_MANAGER')")
    @GetMapping("/nearby")
    @Operation(
            summary = "Yaqin atrofdagi salonlarni qidirish",
            description = "Latitude, longitude va radius bo'yicha yaqin atrofdagi salonlar ro'yxatini qaytaradi. Barcha role uchun."
    )
    public ResponseEntity<ApiResult<List<SalonListDto>>> findNearby(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5") Double radiusKm,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<List<SalonListDto>> result = salonService.findNearby(latitude, longitude, radiusKm, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MASTER', 'SALON_MANAGER')")
    @GetMapping("/search")
    @Operation(
            summary = "Salonlarni qidirish",
            description = "So'rov va kategoriya bo'yicha salonlarni qidiradi. Barcha role uchun."
    )
    public ResponseEntity<ApiResult<List<SalonListDto>>> search(
            @RequestParam String query,
            @RequestParam(required = false) Long category,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<List<SalonListDto>> result = salonService.search(query, category, language);
        return ResponseEntity.ok(result);
    }
}