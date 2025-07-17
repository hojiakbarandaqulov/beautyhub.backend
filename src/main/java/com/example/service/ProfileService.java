package com.example.service;

import com.example.dto.base.ApiResult;
import com.example.dto.profile.ProfileSavePhoto;
import com.example.dto.profile.ProfileUpdatePasswordDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.LanguageEnum;
import com.example.exp.AppBadException;
import com.example.repository.ProfileRepository;
import com.example.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AttachService attachService;
    private final ResourceBundleService messageService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApiResult<String>updatePhoto(String photoId, LanguageEnum language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        ProfileEntity profile = getById(profileId);
        if (profile.getPhotoId() != null && profile.getPhotoId().equals(photoId)) {
            attachService.delete(profile.getPhotoId());
        }
        profileRepository.updateProfilePhoto(profileId, photoId);
        return new ApiResult<String>(messageService.getMessage("photo.update.success", language));
    }


    public ApiResult<String> updatePassword(ProfileUpdatePasswordDTO profileDTO, LanguageEnum language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        ProfileEntity profile = getById(profileId);
        if (!bCryptPasswordEncoder.matches(profileDTO.getCurrentPassword(), profile.getPassword())) {
            throw new AppBadException(messageService.getMessage("wrong.password", language));
        }
        profileRepository.updatePassword(profileId, bCryptPasswordEncoder.encode(profileDTO.getNewPassword()));
        return new ApiResult<String>(messageService.getMessage("profile.password.update.success", language));
    }

    public ProfileEntity getById(Long id) {
        return profileRepository.findByIdAndVisibleTrue(id).orElseThrow(() -> new AppBadException("Profile not found"));
    }
}
