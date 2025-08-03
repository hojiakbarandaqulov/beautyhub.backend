package com.example.service;

import com.example.dto.base.ApiResult;
import com.example.dto.language.LanguageUpdateDto;
import com.example.dto.profile.*;
import com.example.entity.ProfileEntity;
import com.example.entity.ProfileRoleEntity;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
    private final CityService cityService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${attach.upload.url}")
    public String attachUrl;
    @Value("${server.url}")
    private String serverUrl;

    public ApiResult<String> updatePhoto(String photoId, LanguageEnum language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        ProfileEntity profile = getById(profileId, language);
        if (profile.getPhotoId() != null && profile.getPhotoId().equals(photoId)) {
            attachService.delete(profile.getPhotoId(),language);
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
        if (!profileDTO.getNewPassword().equals(profileDTO.getConfirmPassword())) {
            throw new AppBadException(messageService.getMessage("wrong.password", language));
        }
        profileRepository.updatePassword(profileId, bCryptPasswordEncoder.encode(profileDTO.getNewPassword()));

        Map<LanguageEnum, String> messages = new HashMap<>();
        messages.put(LanguageEnum.uz, messageService.getMessage("profile.password.update.success", LanguageEnum.uz));
        messages.put(LanguageEnum.ru, messageService.getMessage("profile.password.update.success", LanguageEnum.ru));
        messages.put(LanguageEnum.en, messageService.getMessage("profile.password.update.success", LanguageEnum.en));
        ApiResult<String> response = new ApiResult<>("Success", messages);
        return new ApiResult<>(response).getData();
    }

    public ApiResult<ProfileUpdateResponseDto> updateProfile(ProfileUpdateDto dto, LanguageEnum lang) {
        Long profileId = SpringSecurityUtil.getProfileId();
        ProfileEntity profile=getById(profileId,lang);

        if (profile.getPhotoId() != null && profile.getPhotoId().equals(dto.getPhotoId())) {
            attachService.delete(profile.getPhotoId(),lang);
        }
        profile.setFullName(dto.getFullName());
        profile.setNotificationsEnabled(dto.getNotifications());
        profile.setPhotoId(dto.getPhotoId());
        profileRepository.save(profile);
        ProfileUpdateResponseDto responseDto = new ProfileUpdateResponseDto();
        responseDto.setId(profile.getId());
        responseDto.setFullName(profile.getFullName());
        responseDto.setNotifications(profile.getNotificationsEnabled());
        responseDto.setPhotoUrl(serverUrl + "/attach/upload/" + profile.getPhotoId());

        responseDto.setRole(profileRoleRepository.getAllRolesListByProfileId(profile.getId()));
        return ApiResult.successResponse(responseDto);
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
        ProfileEntity profile = getById(profileId, language);
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

    public ApiResult<String> updateLanguage(LanguageUpdateDto dto) {
        Long profileId = SpringSecurityUtil.getProfileId();
        ProfileEntity profile = getById(profileId, dto.getLanguageCode());
        profile.setLanguage(dto.getLanguageCode());
        profileRepository.save(profile);

        Map<LanguageEnum, String> messages = new HashMap<>();
        messages.put(LanguageEnum.uz, messageService.getMessage("profile.update.language", LanguageEnum.uz));
        messages.put(LanguageEnum.ru, messageService.getMessage("profile.update.language", LanguageEnum.ru));
        messages.put(LanguageEnum.en, messageService.getMessage("profile.update.language", LanguageEnum.en));
        ApiResult<String> response = new ApiResult<>("Success", messages);
        return new ApiResult<>(response).getData();
    }

    public ProfileEntity findByPhone(String name) {
        Optional<ProfileEntity> byPhoneAndVisibleTrue = profileRepository.findByPhoneAndVisibleTrue(name);
        if (byPhoneAndVisibleTrue.isPresent()) {
            return byPhoneAndVisibleTrue.get();
        }
        throw new AppBadException(messageService.getMessage("profile.not.found", LanguageEnum.uz));
    }

    public ProfileEntity getById(Long id, LanguageEnum language) {
        return profileRepository.findByIdAndVisibleTrue(id).orElseThrow(() -> new AppBadException(messageService.getMessage("profile.not.found", language)));
    }

    public ProfileEntity findById(Long recipientId) {
        Optional<ProfileEntity> byId = profileRepository.findByRecipientId(recipientId);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new AppBadException(messageService.getMessage("profile.not.found", LanguageEnum.en));
    }

    public ApiResult<ProfileUpdateResponseDto> getProfileDetail(LanguageEnum language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        ProfileEntity profile = getById(profileId, language);
        ProfileUpdateResponseDto responseDto = new ProfileUpdateResponseDto();
        responseDto.setId(profile.getId());
        responseDto.setFullName(profile.getFullName());
        responseDto.setNotifications(profile.getNotificationsEnabled());
        responseDto.setPhotoUrl(serverUrl + "/attach/upload/" + profile.getPhotoId());

        responseDto.setRole(profileRoleRepository.getAllRolesListByProfileId(profile.getId()));

        return ApiResult.successResponse(responseDto);
    }

    public ApiResult<String> addRole(Long profileId, ProfileRole role, LanguageEnum language) {
        ProfileEntity profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("profile.not.found", language)));

        // Agar role allaqachon mavjud bo'lsa, qayta qo'shmaymiz
        List<ProfileRole> existingRoles = profileRoleRepository.getAllRolesListByProfileId(profileId);
        if (existingRoles.contains(role)) {
            throw new AppBadException(messageService.getMessage("role.already.exists", language));
        }

        // Yangi role qo‘shish
        ProfileRoleEntity profileRoleEntity = new ProfileRoleEntity();
        profileRoleEntity.setProfileId(profile.getId());
        profileRoleEntity.setRoles(role);
        profileRoleEntity.setCreatedDate(LocalDateTime.now());
        profileRoleRepository.save(profileRoleEntity);
        Map<LanguageEnum, String> messages = new HashMap<>();
        messages.put(LanguageEnum.uz, messageService.getMessage("role.successfully.create", LanguageEnum.uz));
        messages.put(LanguageEnum.ru, messageService.getMessage("role.successfully.create", LanguageEnum.ru));
        messages.put(LanguageEnum.en, messageService.getMessage("role.successfully.create", LanguageEnum.en));
        ApiResult<String> response = new ApiResult<>("Success", messages);
        return new ApiResult<>(response).getData();
    }

    public ApiResult<String> removeRole(Long profileId, ProfileRole role, LanguageEnum language) {
        ProfileEntity profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("profile.not.found", language)));

        // Role mavjudligini tekshirish
        Optional<ProfileRoleEntity> roleEntityOpt = profileRoleRepository.findByProfileIdAndRoles(profileId, role);
        if (roleEntityOpt.isEmpty()) {
            throw new AppBadException(messageService.getMessage("profile.role.not.found", language));
        }

        // Role’ni o‘chirish
        profileRoleRepository.delete(roleEntityOpt.get());
        Map<LanguageEnum, String> messages = new HashMap<>();
        messages.put(LanguageEnum.uz, messageService.getMessage("role.successfully.clear", LanguageEnum.uz));
        messages.put(LanguageEnum.ru, messageService.getMessage("role.successfully.clear", LanguageEnum.ru));
        messages.put(LanguageEnum.en, messageService.getMessage("role.successfully.clear", LanguageEnum.en));
        ApiResult<String> response = new ApiResult<>("Success", messages);
        return new ApiResult<>(response).getData();
    }

    public List<ProfileRole> getRoles(Long profileId, LanguageEnum language) {
        profileRepository.findById(profileId)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("profile.not.found", language)));
        return profileRoleRepository.getAllRolesListByProfileId(profileId);
    }

    public ApiResult<ProfileUpdateCity> updateProfileCity(ProfileUpdateCity dto, LanguageEnum language) {
        Long profileId = SpringSecurityUtil.getProfileId();

        ProfileEntity profileEntity = getById(profileId, language);
        profileEntity.setCityId(dto.getCityId());
        profileRepository.save(profileEntity);
        ProfileUpdateCity responseDto = new ProfileUpdateCity();
        responseDto.setCityId(dto.getCityId());
        return ApiResult.successResponse(responseDto);
    }
}
