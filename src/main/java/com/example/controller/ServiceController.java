package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.base.ApiResult;
import com.example.dto.service.ServiceCreateRequest;
import com.example.dto.service.ServiceResponse;
import com.example.dto.service.ServiceUpdateRequest;
import com.example.entity.home_pages.ServiceEntity;
import com.example.enums.LanguageEnum;
import com.example.exp.AppBadException;
import com.example.mapper.ServiceMapper;
import com.example.repository.ServiceRepository;
import com.example.service.impl.ServiceService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/services")
public class ServiceController {
    private final ServiceService serviceService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    public ResponseEntity<ApiResult<ServiceResponse>> createService(
           @Valid @RequestBody ServiceCreateRequest request,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<ServiceResponse> apiResult = serviceService.create(request, language);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER', 'MASTER', 'USER')")
    public ResponseEntity<ApiResult<ServiceResponse>> getServiceById(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<ServiceResponse> byId = serviceService.getById(id, language);
        return ResponseEntity.ok(byId);
    }

    @GetMapping("/salon/{salonId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER', 'MASTER', 'USER')")
    public ResponseEntity<ApiResult<List<ServiceResponse>>> getServicesBySalon(
            @PathVariable Long salonId,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<List<ServiceResponse>> apiResult = serviceService.getBySalonId(salonId, language);
        return ResponseEntity.ok(apiResult);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    public ResponseEntity<ApiResult<ServiceResponse>> updateService(
            @PathVariable Long id,
            @RequestBody ServiceUpdateRequest request,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<ServiceResponse> apiResult = serviceService.update(id, request, language);
        return ResponseEntity.ok(apiResult);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALON_MANAGER')")
    public ResponseEntity<ApiResult<String>> deleteService(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<String> apiResult = serviceService.delete(id, language);
        return ResponseEntity.ok(apiResult);
    }

}