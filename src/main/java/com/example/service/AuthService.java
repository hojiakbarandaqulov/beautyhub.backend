package com.example.service;

import com.example.dto.RegistrationDTO;
import com.example.dto.base.ApiResult;
import com.example.entity.ProfileEntity;
import com.example.enums.GeneralStatus;
import com.example.enums.LanguageEnum;
import com.example.enums.ProfileRole;
import com.example.exp.AppBadException;
import com.example.repository.ProfileRepository;
import com.example.service.sms.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
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
    private final ResourceBundleMessageSource messageSource;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SmsService smsService;

    public ApiResult<String> registration(RegistrationDTO registrationDTO, LanguageEnum language) {
        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(registrationDTO.getPhoneNumber());
        if (optional.isPresent()) {
            ProfileEntity profileEntity = optional.get();
            if (profileEntity.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
                profileRepository.delete(profileEntity);
            } else {
                throw new AppBadException(messageSource.getMessage("email.phone.exists",null,new Locale(language.name())));
            }
        }
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            log.warn("Password does not match confirm password => {}", registrationDTO.getConfirmPassword());
            String message = messageSource.getMessage("password.not.match", null, new Locale(language.name()));
            throw new AppBadException(message);
        }

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setFullName(profileEntity.getFullName());
        profileEntity.setPhone(registrationDTO.getPhoneNumber());
        profileEntity.setPassword(bCryptPasswordEncoder.encode(registrationDTO.getPassword()));
        profileEntity.setRole(ProfileRole.USER);
        profileEntity.setStatus(GeneralStatus.IN_REGISTRATION);
        profileEntity.setVisible(true);
        profileEntity.setCreatedDate(LocalDateTime.now());
        profileRepository.save(profileEntity);
        smsService.sendSms(profileEntity.getPhone());
        return ApiResult.successResponse();
    }
}
