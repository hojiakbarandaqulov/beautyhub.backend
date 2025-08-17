package com.example.controller;

import com.example.dto.auth.*;
import com.example.dto.base.ApiResponse;
import com.example.dto.base.ApiResult;
import com.example.dto.profile.ProfileDTO;
import com.example.dto.reset.ResetPasswordConfirmDTO;
import com.example.dto.reset.ResetPasswordDTO;
import com.example.enums.LanguageEnum;
import com.example.exp.AppBadException;
import com.example.service.AuthService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration")
    public ResponseEntity<ApiResult<String>> registration(@RequestBody @Valid RegistrationDTO registrationDTO,
                                                          @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<String> apiResult = authService.registration(registrationDTO, language);
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/registration/login")
    public ResponseEntity<ApiResult<LoginResponseDTO>> login(@Valid @RequestBody LoginDTO dto,
                                                             @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<LoginResponseDTO> ok = authService.login(dto, language);
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/registration/verification")
    public ResponseEntity<ProfileDTO> registrationVerification(@RequestBody @Valid SmsVerificationDTO smsVerification,
                                                               @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum lang) {
        ProfileDTO ok = authService.regVerification(smsVerification, lang);
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/registration/reset")
    public ResponseEntity<ApiResult<String>> resent(@Valid @RequestBody ResetPasswordDTO dto,
                                                    @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<String> ok = authService.resetSms(dto, language);
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/registration/reset-password/confirm")
    public ResponseEntity<ApiResult<String>> resentPassword(@Valid @RequestBody ResetPasswordConfirmDTO dto,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<String> ok = authService.resetPasswordConfirm(dto, language);
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/refresh-token")
    @PreAuthorize("hasAnyRole('USER','ADMIN','MASTER','SALON_MANAGER')")
    public ApiResult<RefreshTokenResponseDTO> refreshToken(@RequestBody @Valid TokenRefreshRequest request) {
        ApiResult<RefreshTokenResponseDTO> apiResult = authService.refreshToken(request);
        return ApiResult.successResponse(apiResult.getData());
    }
}

