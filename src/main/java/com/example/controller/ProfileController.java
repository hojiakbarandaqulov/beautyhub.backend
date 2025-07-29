package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.language.LanguageUpdateDto;
import com.example.dto.profile.*;
import com.example.enums.LanguageEnum;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PutMapping("/update/photo")
    public ResponseEntity<ApiResult<String>> updatePhoto(@Valid @RequestBody ProfileSavePhoto photo,
                                                         @RequestHeader(value = "Accept-Language",defaultValue = "ru") LanguageEnum language) {
        ApiResult<String> apiResult=profileService.updatePhoto(photo.getPhotoId(),language);
        return ResponseEntity.ok(apiResult);
    }

    @PutMapping("/update/password")
    public ResponseEntity<ApiResult<String>> updatePassword(@Valid @RequestBody ProfileUpdatePasswordDTO profileDTO,
                                                              @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<String> apiResponse = profileService.updatePassword(profileDTO, language);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update/phone")
    public ResponseEntity<ApiResult<String>> updateUsername(@Valid @RequestBody ProfileUpdatePhoneDTO profileDTO,
                                                              @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<String> apiResponse = profileService.updatePhone(profileDTO, language);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/update/photo/confirm")
    public ResponseEntity<ApiResult<String>> updateConfirm(@Valid @RequestBody CodeConfirmDTO dto,
                                                             @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        ApiResult<String> apiResponse = profileService.updatePhoneConfirm(dto, language);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/update/language/confirm")
    public ResponseEntity<ApiResult<String>> updateLanguage(@Valid @RequestBody LanguageUpdateDto dto) {
        ApiResult<String> apiResponse = profileService.updateLanguage(dto);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/all/detail")
    public ResponseEntity<ApiResult<ProfileDetailDTO>>profileDetail(@RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
       ApiResult<ProfileDetailDTO>apiResult= profileService.getProfileDetail(language);
        return ResponseEntity.ok(apiResult);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/role/add/{profileId}")
    public ResponseEntity<ApiResult<String>> addRole(
            @PathVariable Long profileId,
            @RequestParam ProfileRole role,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
       ApiResult<String>apiResult= profileService.addRole(profileId, role, language);
        return ResponseEntity.ok(apiResult);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/roles/remove/{profileId}")
    public ResponseEntity<ApiResult<String>> removeRole(
            @PathVariable Long profileId,
            @RequestParam ProfileRole role,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
       ApiResult<String>apiResult= profileService.removeRole(profileId, role, language);
        return  ResponseEntity.ok(apiResult);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/roles/{profileId}")
    public ApiResult<List<ProfileRole>> getRoles(
            @PathVariable Long profileId,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        return ApiResult.successResponse(profileService.getRoles(profileId,language));
    }
}
