package com.example.controller;

import com.example.dto.auth.LoginResponseDTO;
import com.example.dto.auth.RegistrationDTO;
import com.example.dto.auth.SmsVerificationDTO;
import com.example.dto.auth.LoginDTO;
import com.example.dto.base.ApiResponse;
import com.example.dto.base.ApiResult;
import com.example.dto.profile.ProfileDTO;
import com.example.dto.reset.ResetPasswordConfirmDTO;
import com.example.dto.reset.ResetPasswordDTO;
import com.example.enums.LanguageEnum;
import com.example.service.AuthService;
import com.example.service.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
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

    @GetMapping("/registration/verification")
    public ResponseEntity<ProfileDTO> registrationVerification(@RequestBody SmsVerificationDTO smsVerification,
                                                               @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum lang) {
        ProfileDTO ok = authService.regVerification(smsVerification, lang);
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/registration/reset")
    public ResponseEntity<ApiResult<String>> resent(@Valid @RequestBody ResetPasswordDTO dto,
                                                    @RequestHeader(defaultValue = "ru") LanguageEnum language) {
        ApiResult<String> ok = authService.resetPassword(dto, language);
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/registration/reset-password/confirm")
    public ResponseEntity<ApiResult<String>> resentPassword(@Valid @RequestBody ResetPasswordConfirmDTO dto,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<String> ok = authService.resetPasswordConfirm(dto, language);
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // 1. Cookie ni tozalash
        Cookie cookie = new Cookie("JWT", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        // 2. Frontendga yaroqsizlanish haqida ma'lumot
        return ResponseEntity.ok()
                .header("Clear-Token", "true")
                .body(true);
    }

}

