package com.example.service;

import com.example.dto.auth.LoginResponseDTO;
import com.example.dto.auth.RegistrationDTO;
import com.example.dto.auth.LoginDTO;
import com.example.dto.auth.SmsVerificationDTO;
import com.example.dto.base.ApiResponse;
import com.example.dto.base.ApiResult;
import com.example.dto.profile.ProfileDTO;
import com.example.dto.reset.ResetPasswordConfirmDTO;
import com.example.dto.reset.ResetPasswordDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.GeneralStatus;
import com.example.enums.LanguageEnum;
import com.example.enums.ProfileRole;
import com.example.exp.AppBadException;
import com.example.repository.ProfileRepository;
import com.example.repository.ProfileRoleRepository;
import com.example.service.sms.SmsHistoryService;
import com.example.service.sms.SmsService;
import com.example.util.JwtUtil;
import com.example.util.PhoneUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {
    private final ProfileRepository profileRepository;
    private final ResourceBundleService messageSource;
    private final ProfileRoleService profileRoleService;
    private final ProfileRoleRepository profileRoleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SmsService smsService;
    private final SmsHistoryService smsHistoryService;

    @Transactional
    public ApiResult<String> registration(RegistrationDTO registrationDTO, LanguageEnum language) {
        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(registrationDTO.getPhone());
        if (optional.isPresent()) {
            ProfileEntity profileEntity = optional.get();
            if (profileEntity.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
                profileRoleService.deleteRoles(profileEntity.getId());
                profileRepository.delete(profileEntity);
            } else {
                throw new AppBadException(messageSource.getMessage("email.phone.exists", language));
            }
        }
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            log.warn("Password does not match confirm password => {}", registrationDTO.getConfirmPassword());
            String message = messageSource.getMessage("password.not.match", language);
            throw new AppBadException(message);
        }

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setFullName(registrationDTO.getFullName());
        profileEntity.setPhone(registrationDTO.getPhone());
        profileEntity.setPassword(bCryptPasswordEncoder.encode(registrationDTO.getPassword()));
        profileEntity.setStatus(GeneralStatus.IN_REGISTRATION);
        profileEntity.setVisible(true);
        profileEntity.setCreatedDate(LocalDateTime.now());
        profileRepository.save(profileEntity);

        profileRoleService.create(profileEntity.getId(), ProfileRole.ROLE_USER);
        smsService.sendSms(profileEntity.getPhone());
        return new ApiResult<>(messageSource.getMessage("phone.sms.send", language));
    }

    public ApiResult<LoginResponseDTO> login(LoginDTO loginDTO, LanguageEnum language) {
        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(loginDTO.getPhone());
        if (optional.isEmpty()) {
            throw new AppBadException(messageSource.getMessage("phone.not.found", language));
        }
        ProfileEntity profile = optional.get();
        if (!bCryptPasswordEncoder.matches(loginDTO.getPassword(), profile.getPassword())) {
            throw new AppBadException(messageSource.getMessage("wrong.password", language));
        }
        if (!profile.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new AppBadException(messageSource.getMessage("wrong.status", language));
        }

        LoginResponseDTO response = new LoginResponseDTO();
        response.setRole(profileRoleRepository.getAllRolesListByProfileId(profile.getId()));
        response.setJwt(JwtUtil.encode(profile.getPhone(), profile.getId(), response.getRole()));
        response.setRefreshToken(JwtUtil.generateRefreshToken(profile.getPhone(), profile.getId()));
        return new ApiResult<>(response);
    }

    public ProfileDTO regVerification(SmsVerificationDTO dto, LanguageEnum lang) {
        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(dto.getPhone());
        if (optional.isEmpty()) {
            throw new AppBadException(messageSource.getMessage("phone.not.found", lang));
        }

        ProfileEntity profile = optional.get();
        if (!profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
            throw new AppBadException(messageSource.getMessage("wrong.status", lang));
        }
        if (!smsHistoryService.checkSmsCode(dto.getPhone(), dto.getCode())) {
            throw new AppBadException("smsCode.invalid", null, new Locale(lang.name()));
        }
        profileRepository.changeStatus(profile.getId(), GeneralStatus.ACTIVE);
        ProfileDTO response = new ProfileDTO();
        response.setFullName(profile.getFullName());
        response.setPhone(profile.getPhone());
        response.setRole(profileRoleRepository.getAllRolesListByProfileId(profile.getId()));
        response.setJwt(JwtUtil.encode(profile.getPhone(), profile.getId(), response.getRole()));
        return getLogResponse(profile);
    }

    public ProfileDTO getLogResponse(ProfileEntity profile) {
        ProfileDTO response = new ProfileDTO();
        response.setFullName(profile.getFullName());
        response.setPhone(profile.getPhone());
        response.setRole(profileRoleRepository.getAllRolesListByProfileId(profile.getId()));
        response.setJwt(JwtUtil.encode(profile.getPhone(), profile.getId(), response.getRole()));
        return response;
    }

    public ApiResult<String> resetPassword(ResetPasswordDTO dto, LanguageEnum language) {
        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(dto.getPhone());
        if (optional.isEmpty()) {
            throw new AppBadException(messageSource.getMessage("phone.password.wrong", language));
        }
        ProfileEntity profile = optional.get();
        if (!profile.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new AppBadException(messageSource.getMessage("wrong.status", language));
        }
        smsService.sendSms(profile.getPhone());
        return new ApiResult<String>(messageSource.getMessage("reset.password.response", language));
    }

    public ApiResult<String> resetPasswordConfirm(ResetPasswordConfirmDTO dto, LanguageEnum language) {
        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(dto.getPhone());
        if (optional.isEmpty()) {
            throw new AppBadException(messageSource.getMessage("verification.wrong", language));
        }
        ProfileEntity profile = optional.get();
        if (!profile.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new AppBadException(messageSource.getMessage("wrong.status", language));
        }
        if (PhoneUtil.isPhone(dto.getPhone())) {
            smsService.sendSms(dto.getPhone());
        } else {
            throw new AppBadException("phone.not.valid.number");
        }
        profileRepository.updatePassword(profile.getId(), bCryptPasswordEncoder.encode(dto.getPassword()));
        return new ApiResult<String>(messageSource.getMessage("reset.password.success", language));
    }
/*
    public String getAuthenticatedUsername() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName)
                .orElseThrow(() -> new AppBadException("Foydalanuvchi avtorizatsiyadan o'tmagan"));
    }

    @Transactional
    public Boolean logout(String username, String token) {
        if (token != null) {
            jwtService.invalidateToken(token);
            System.out.println("JWT token bekor qilindi: " + token);
        } else {
            System.out.println("Logout so'rovi keldi, lekin token topilmadi. Faqat foydalanuvchi konteksti tozalandi.");
        }
        profileRepository.findByPhoneAndVisibleTrue(username).ifPresent(user -> {
            user.setVisible(false); // Foydalanuvchini "ko'rinmas" yoki "offline" deb belgilash
            profileRepository.save(user); // O'zgarishlarni saqlash
            System.out.println("Foydalanuvchi " + username + " statusi 'ko'rinmas' deb yangilandi.");
        });
        System.out.println("Foydalanuvchi " + username + " tizimdan chiqish mantig'i bajarildi.");
        return true;
    }*/
}

