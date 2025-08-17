package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.service.ServiceCreateRequest;
import com.example.dto.service.ServiceResponse;
import com.example.dto.service.ServiceUpdateRequest;
import com.example.enums.LanguageEnum;
import com.example.service.impl.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/services")
@Tag(name = "Xizmat", description = "Xizmatlarni yaratish, tahrirlash, o'chirish va ko'rish uchun APIlar")
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    @Operation(
            summary = "Yangi xizmat yaratish",
            description = "Yangi xizmat (masalan, soch olish, manikyur va h.k.) ni tizimga qo'shadi. Faqat admin va salon managerlar uchun."
    )
    public ResponseEntity<ApiResult<ServiceResponse>> createService(
            @RequestBody ServiceCreateRequest request,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<ServiceResponse> apiResult = serviceService.create(request, language);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER', 'MASTER', 'USER')")
    @Operation(
            summary = "Xizmat tafsilotlari",
            description = "Berilgan id bo'yicha xizmatning to'liq ma'lumotlarini qaytaradi. Barcha role uchun."
    )
    public ResponseEntity<ApiResult<ServiceResponse>> getServiceById(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<ServiceResponse> byId = serviceService.getById(id, language);
        return ResponseEntity.ok(byId);
    }

    @GetMapping("/salon/{salonId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER', 'MASTER', 'USER')")
    @Operation(
            summary = "Salon xizmatlari ro'yxati",
            description = "Berilgan salonId bo'yicha shu salonda mavjud xizmatlarni ro'yxatini qaytaradi. Barcha role uchun."
    )
    public ResponseEntity<ApiResult<List<ServiceResponse>>> getServicesBySalon(
            @PathVariable Long salonId,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<List<ServiceResponse>> apiResult = serviceService.getBySalonId(salonId, language);
        return ResponseEntity.ok(apiResult);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    @Operation(
            summary = "Xizmatni tahrirlash",
            description = "Berilgan id bo'yicha xizmat ma'lumotlarini yangilaydi. Faqat admin va salon managerlar uchun."
    )
    public ResponseEntity<ApiResult<ServiceResponse>> updateService(
            @PathVariable Long id,
            @RequestBody ServiceUpdateRequest request,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<ServiceResponse> apiResult = serviceService.update(id, request, language);
        return ResponseEntity.ok(apiResult);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    @Operation(
            summary = "Xizmatni o'chirish",
            description = "Berilgan id bo'yicha xizmatni tizimdan o'chiradi. Faqat admin va salon managerlar uchun."
    )
    public ResponseEntity<ApiResult<String>> deleteService(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<String> apiResult = serviceService.delete(id, language);
        return ResponseEntity.ok(apiResult);
    }
}