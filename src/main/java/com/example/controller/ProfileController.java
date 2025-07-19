package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.profile.ProfileSavePhoto;
import com.example.dto.profile.ProfileUpdatePasswordDTO;
import com.example.enums.LanguageEnum;
import com.example.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PutMapping("/update/photo")
    public ResponseEntity<ApiResult<String>> updatePhoto(@Valid @RequestBody ProfileSavePhoto photo,
                                                         @RequestHeader(value = "Accept-Language",defaultValue = "uz") LanguageEnum language) {
        ApiResult<String> apiResult=profileService.updatePhoto(photo.getPhotoId(),language);
        return ResponseEntity.ok(apiResult);
    }

    @PutMapping("/update/password")
    public ResponseEntity<ApiResult<String>> updatePassword(@Valid @RequestBody ProfileUpdatePasswordDTO profileDTO,
                                                              @RequestHeader(value = "Accept-Language", defaultValue = "uz") LanguageEnum language) {
        ApiResult<String> apiResponse = profileService.updatePassword(profileDTO, language);
        return ResponseEntity.ok(apiResponse);
    }
}
