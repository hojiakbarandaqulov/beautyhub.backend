package com.example.service;

import com.example.dto.base.ApiResult;
import com.example.dto.language.LanguageUpdateDto;
import com.example.dto.profile.CodeConfirmDTO;
import com.example.dto.profile.ProfileUpdatePasswordDTO;
import com.example.dto.profile.ProfileUpdatePhoneDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.LanguageEnum;
import com.example.enums.ProfileRole;
import com.example.exp.AppBadException;
import com.example.repository.ProfileRepository;
import com.example.repository.ProfileRoleRepository;
import com.example.service.sms.SmsHistoryService;
import com.example.service.sms.SmsService;
import com.example.util.JwtUtil;
import com.example.util.PhoneUtil;
import com.example.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileRoleRepository profileRoleRepository;
    private final AttachService attachService;
    private final ResourceBundleService messageService;
    private final SmsService smsService;
    private final SmsHistoryService smsHistoryService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApiResult<String> updatePhoto(String photoId, LanguageEnum language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        ProfileEntity profile = getById(profileId,language);
        if (profile.getPhotoId() != null && profile.getPhotoId().equals(photoId)) {
            attachService.delete(profile.getPhotoId());
        }
        profileRepository.updateProfilePhoto(profileId, photoId);
        Map<LanguageEnum, String> messages = new HashMap<>();
        messages.put(LanguageEnum.uz, messageService.getMessage("photo.update.success", LanguageEnum.uz));
        messages.put(LanguageEnum.ru, messageService.getMessage("photo.update.success", LanguageEnum.ru));
        messages.put(LanguageEnum.en, messageService.getMessage("photo.update.success", LanguageEnum.en));
        ApiResult<String> response = new ApiResult<>("Success", messages);
        return new ApiResult<>(response).getData();
    }


    public ApiResult<String> updatePassword(ProfileUpdatePasswordDTO profileDTO, LanguageEnum language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        ProfileEntity profile = getById(profileId, language);
        if (!bCryptPasswordEncoder.matches(profileDTO.getCurrentPassword(), profile.getPassword())) {
            throw new AppBadException(messageService.getMessage("wrong.password", language));
        }
        profileRepository.updatePassword(profileId, bCryptPasswordEncoder.encode(profileDTO.getNewPassword()));

        Map<LanguageEnum, String> messages = new HashMap<>();
        messages.put(LanguageEnum.uz, messageService.getMessage("profile.password.update.success", LanguageEnum.uz));
        messages.put(LanguageEnum.ru, messageService.getMessage("profile.password.update.success", LanguageEnum.ru));
        messages.put(LanguageEnum.en, messageService.getMessage("profile.password.update.success", LanguageEnum.en));
        ApiResult<String> response = new ApiResult<>("Success", messages);
        return new ApiResult<>(response).getData();
//        return new ApiResult<String>(messageService.getMessage("profile.password.update.success", language));
    }


    public ApiResult<String> updatePhone(ProfileUpdatePhoneDTO profileDTO, LanguageEnum language) {
        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(profileDTO.getPhone());
        if (optional.isPresent()) {
            throw new AppBadException(messageService.getMessage("email.phone.exists", language));
        }
        if (PhoneUtil.isPhone(profileDTO.getPhone())) {
            smsService.sendSms(profileDTO.getPhone());
        }
        Long profileId = SpringSecurityUtil.getProfileId();
        profileRepository.updateTempPhone(profileId, profileDTO.getPhone());

        Map<LanguageEnum, String> messages = new HashMap<>();
        messages.put(LanguageEnum.uz, messageService.getMessage("reset.password.response", LanguageEnum.uz));
        messages.put(LanguageEnum.ru, messageService.getMessage("reset.password.response", LanguageEnum.ru));
        messages.put(LanguageEnum.en, messageService.getMessage("reset.password.response", LanguageEnum.en));
        ApiResult<String> response = new ApiResult<>("Success", messages);
        return new ApiResult<>(response).getData();
    }

    public ApiResult<String> updatePhoneConfirm(CodeConfirmDTO dto, LanguageEnum language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        ProfileEntity profile = getById(profileId,language);
        String tempPhone = profile.getTempPhone();
        if (PhoneUtil.isPhone(tempPhone)) {
            smsHistoryService.checkSmsCode(tempPhone, dto.getCode());
        }
        profileRepository.updatePhone(profileId, tempPhone);
        List<ProfileRole> roles = profileRoleRepository.getAllRolesListByProfileId(profile.getId());
        String jwt = JwtUtil.encode(tempPhone, profile.getId(), roles);

        Map<LanguageEnum, String> messages = new HashMap<>();
        messages.put(LanguageEnum.uz, messageService.getMessage("change.phone.success", LanguageEnum.uz));
        messages.put(LanguageEnum.ru, messageService.getMessage("change.phone.success", LanguageEnum.ru));
        messages.put(LanguageEnum.en, messageService.getMessage("change.phone.success", LanguageEnum.en));
        ApiResult<String> response = new ApiResult<>("Success", messages);
        return new ApiResult<>(response).getData();
    }

    public ProfileEntity getById(Long id,LanguageEnum language) {
        return profileRepository.findByIdAndVisibleTrue(id).orElseThrow(() -> new AppBadException(messageService.getMessage("profile.not.found",language)));
    }

    public ApiResult<String> updateLanguage(LanguageUpdateDto dto) {
        Long profileId = SpringSecurityUtil.getProfileId();
        ProfileEntity profile = getById(profileId,dto.getLanguageCode());
        profile.setLanguage(dto.getLanguageCode());
        profileRepository.save(profile);

        Map<LanguageEnum, String> messages = new HashMap<>();
        messages.put(LanguageEnum.uz, messageService.getMessage("profile.update.language", LanguageEnum.uz));
        messages.put(LanguageEnum.ru, messageService.getMessage("profile.update.language", LanguageEnum.ru));
        messages.put(LanguageEnum.en, messageService.getMessage("profile.update.language", LanguageEnum.en));
        ApiResult<String> response = new ApiResult<>("Success", messages);
        return new ApiResult<>(response).getData();
    }
}
