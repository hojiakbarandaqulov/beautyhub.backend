package com.example.controller;

import com.example.dto.RegistrationDTO;
import com.example.dto.base.ApiResult;
import com.example.enums.LanguageEnum;
import com.example.service.AuthService;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService profileService;

    @PostMapping("/registration")
    public ResponseEntity<ApiResult<String>> registration(@RequestBody @Valid RegistrationDTO registrationDTO,
                                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ") LanguageEnum language){
       ApiResult<String> apiResult= profileService.registration(registrationDTO,language);
       return ResponseEntity.ok(apiResult);
    }
}
