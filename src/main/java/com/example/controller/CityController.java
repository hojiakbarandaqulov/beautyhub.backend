package com.example.controller;

import com.example.dto.city.*;
import com.example.dto.base.ApiResult;
import com.example.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/city")
@Tag(name = "Shahar", description = "Shaharlar bilan ishlash uchun API: yaratish, tahrirlash, barcha shaharlani ko'rish va qidirish")
public class CityController {

    private final CityService cityService;

    @PostMapping(value = "/create")
    @Operation(
            summary = "Yangi shahar yaratish",
            description = "Yangi shahar ma'lumotlarini (nomi va boshqalar) yuborib, tizimga yangi shahar qo'shadi."
    )
    public ResponseEntity<ApiResult<CityResponseDTO>> create(@Valid @RequestBody CityCreateDTO region){
        ApiResult<CityResponseDTO> response = cityService.createCity(region);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/update")
    @Operation(
            summary = "Shahar ma'lumotlarini tahrirlash",
            description = "Shahar ma'lumotlarini yangilash. Yangi nom yoki boshqa ma'lumotlar bilan shaharni tahrirlaydi."
    )
    public ResponseEntity<ApiResult<Boolean>> updateCity(@Valid @RequestBody CityUpdateDTO region){
        ApiResult<Boolean> response = cityService.updateCity(region);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cityAll")
    @Operation(
            summary = "Barcha shaharlar ro'yxati",
            description = "Tizimdagi barcha shaharlarni ro'yxatini qaytaradi. Faqat USER roli uchun."
    )
    public ResponseEntity<ApiResult<List<CityResponseAllDTO>>> all() {
        return ResponseEntity.ok().body(cityService.getAll());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search")
    @Operation(
            summary = "Shaharlarni qidirish",
            description = "Query orqali shaharlarni qidiradi va natijani qaytaradi. Tilni ham tanlash mumkin."
    )
    public ResponseEntity<ApiResult<SearchResultDTO>> search(
            @RequestParam String query,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") String language) {

        ApiResult<SearchResultDTO> result = cityService.citySearch(query, language);
        return ResponseEntity.ok(result);
    }
}