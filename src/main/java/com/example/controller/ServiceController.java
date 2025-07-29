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
    import lombok.RequiredArgsConstructor;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/api/v1/services")
    public class ServiceController {
        private final ServiceService serviceService;
        private final ServiceMapper serviceMapper;


        @PostMapping("/create")
        public ApiResult<ServiceResponse> createService(
                @RequestBody ServiceCreateRequest request,
                @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
            return serviceService.create(request, language);
        }

        @GetMapping("/{id}")
        public ApiResult<ServiceResponse> getServiceById(
                @PathVariable Long id,
                @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
            return serviceService.getById(id, language);
        }

        @GetMapping("/salon/{salonId}")
        public ApiResult<List<ServiceResponse>> getServicesBySalon(
                @PathVariable Long salonId,
                @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
            return serviceService.getBySalonId(salonId, language);
        }

        @PutMapping("/{id}")
        public ApiResult<ServiceResponse> updateService(
                @PathVariable Long id,
                @RequestBody ServiceUpdateRequest request,
                @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
            return serviceService.update(id, request, language);
        }

        @DeleteMapping("/{id}")
        public ApiResult<String> deleteService(
                @PathVariable Long id,
                @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
            return serviceService.delete(id, language);
        }
    }