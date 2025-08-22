package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.salon.*;
import com.example.service.service_interface.SalonService;
import com.example.enums.LanguageEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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


    @GetMapping("/get/{id}")
    @Operation(
            summary = "Salon tafsilotlari",
            description = "Berilgan id bo'yicha salonning to'liq ma'lumotlarini qaytaradi. Barcha role uchun."
    )
    public ResponseEntity<ApiResult<SalonListDto>> getById(@PathVariable Long id,
                                                           @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<SalonListDto> result = salonService.getById(id, language);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAll")
    @Operation(
            summary = "Barcha salonlar ro'yxati",
            description = "Tizimdagi barcha salonlar ro'yxatini qaytaradi. Barcha role uchun."
    )
    public ResponseEntity<ApiResult<List<SalonListDto>>> getAll() {
        ApiResult<List<SalonListDto>> result = salonService.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping
    @Operation(
            summary = "Barcha salonlar ro'yxati pagination bilan",
            description = "Tizimdagi barcha salonlar ro'yxatini qaytaradi. Barcha role uchun."
    )
    public ResponseEntity<ApiResult<PageImpl<SalonListDto>>> salonPagination(@RequestParam(value = "page",defaultValue = "1")int page,
                                                                             @RequestParam(value = "size",defaultValue = "10")int size) {
        ApiResult<PageImpl<SalonListDto>> result = salonService.getSalons(page-1,size);
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
            @RequestParam(value = "page",defaultValue = "1")int page,
            @RequestParam(value = "size", defaultValue = "10")int size,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<List<SalonListDto>> result = salonService.search(query, category,page-1,size, language);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/outdoor")
    @PermitAll
    public ResponseEntity<ApiResult<Page<SalonListDto>>> getOutdoor(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResult.successResponse(salonService.getOutdoor(page, size)));
    }

    // 🔹 Bolalar uchun
    @GetMapping("/for-kids")
    @PermitAll
    public ResponseEntity<ApiResult<Page<SalonListDto>>> getForKids(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResult.successResponse(salonService.getForKids(page, size)));
    }
}