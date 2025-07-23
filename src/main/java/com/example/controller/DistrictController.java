package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.city.DistrictResponseLanguageDTO;
import com.example.enums.LanguageEnum;
import com.example.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/district")
public class DistrictController {

    private final DistrictService districtService;

    /*@PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResult<String>> updateDistrict(@PathVariable Long id,
                                                            @RequestBody DistrictUpdate districtUpdate,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "ru")LanguageEnum language){
       ApiResult<String>apiResponse= districtService.updateDistrict(id,districtUpdate,language);
       return ResponseEntity.ok(apiResponse);
    }
*/
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/district/{id}")
    public ResponseEntity<ApiResult<List<DistrictResponseLanguageDTO>>> district(@PathVariable Long id,
                                                                                 @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<List<DistrictResponseLanguageDTO>> apiResult = districtService.getAllByLang(id, language);
        return ResponseEntity.ok(apiResult);
    }

}
