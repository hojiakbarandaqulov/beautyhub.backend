package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.city.DistrictResponseLanguageDTO;
import com.example.enums.LanguageEnum;
import com.example.service.DistrictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/district")
@Tag(name = "Tuman", description = "Tumanlar bilan ishlash uchun API: shahar bo'yicha tumanlarni olish")
public class DistrictController {

    private final DistrictService districtService;

    /*
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{id}")
    @Operation(
        summary = "Tumanni tahrirlash",
        description = "Berilgan id bo'yicha tumanni tahrirlaydi. Faqat USER roli uchun."
    )
    public ResponseEntity<ApiResult<String>> updateDistrict(@PathVariable Long id,
                                                            @RequestBody DistrictUpdate districtUpdate,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language){
       ApiResult<String>apiResponse= districtService.updateDistrict(id,districtUpdate,language);
       return ResponseEntity.ok(apiResponse);
    }
    */

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/district/{id}")
    @Operation(
            summary = "Shaharga tegishli tumanlar ro'yxati",
            description = "Berilgan shahar id bo'yicha barcha tumanlarni tanlangan til bo'yicha ro'yxatini qaytaradi. Faqat USER roli uchun."
    )
    public ResponseEntity<ApiResult<List<DistrictResponseLanguageDTO>>> district(@PathVariable Long id,
                                                                                 @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<List<DistrictResponseLanguageDTO>> apiResult = districtService.getAllByLang(id, language);
        return ResponseEntity.ok(apiResult);
    }
}