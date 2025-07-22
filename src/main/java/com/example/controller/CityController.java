package com.example.controller;

import com.example.dto.city.CityCreateDTO;
import com.example.dto.city.CityResponseAllDTO;
import com.example.dto.city.CityResponseDTO;
import com.example.dto.base.ApiResult;
import com.example.service.CityService;
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
public class CityController {

    private final CityService cityService;

    @PostMapping(value = "/create")
    public ResponseEntity<ApiResult<CityResponseDTO>> create(@Valid @RequestBody CityCreateDTO region){
        ApiResult<CityResponseDTO> response=cityService.createCity(region);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cityAll")
    public ResponseEntity<ApiResult<List<CityResponseAllDTO>>> all() {
        return ResponseEntity.ok().body(cityService.getAll());
    }

}
