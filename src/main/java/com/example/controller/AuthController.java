package com.example.controller;

import com.example.dto.auth.RegistrationDTO;
import com.example.dto.auth.SmsVerificationDTO;
import com.example.dto.auth.LoginDTO;
import com.example.dto.base.ApiResult;
import com.example.dto.profile.ProfileDTO;
import com.example.dto.reset.ResetPasswordConfirmDTO;
import com.example.dto.reset.ResetPasswordDTO;
import com.example.enums.LanguageEnum;
import com.example.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService profileService) {
        this.authService = profileService;
    }

    @PostMapping("/registration")
    public ResponseEntity<ApiResult<String>> registration(@RequestBody @Valid RegistrationDTO registrationDTO,
                                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ") LanguageEnum language) {
        ApiResult<String> apiResult = authService.registration(registrationDTO, language);
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/registration/login")
    public ResponseEntity<ApiResult<ProfileDTO>> login(@Valid @RequestBody LoginDTO dto,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "UZ") LanguageEnum language) {
        ApiResult<ProfileDTO> ok = authService.login(dto, language);
        return ResponseEntity.ok(ok);
    }

    @GetMapping("/registration/verification")
    public ResponseEntity<ProfileDTO> registrationVerification(@RequestBody SmsVerificationDTO smsVerification,
                                                               @RequestHeader(value = "Accept-Language", defaultValue = "UZ") LanguageEnum lang) {
        ProfileDTO ok = authService.regVerification(smsVerification, lang);
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/registration/reset")
    public ResponseEntity<ApiResult<String>> resent(@Valid @RequestBody ResetPasswordDTO dto,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "UZ") LanguageEnum language) {
        ApiResult<String> ok = authService.resetPassword(dto, language);
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/registration/reset-password/confirm")
    public ResponseEntity<ApiResult<String>> resentPassword(@Valid @RequestBody ResetPasswordConfirmDTO dto,
                                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") LanguageEnum language) {
        ApiResult<String> ok = authService.resetPasswordConfirm(dto, language);
        return ResponseEntity.ok(ok);
    }

}
