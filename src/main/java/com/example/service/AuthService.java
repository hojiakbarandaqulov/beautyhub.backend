package com.example.service;

import com.example.LanguageEnum;
import com.example.dto.RegistrationDTO;
import com.example.dto.base.ApiResult;
import com.example.entity.ProfileEntity;
import com.example.exp.AppBadException;
import com.example.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {
    private final ProfileRepository profileRepository;
    private final ResourceBundleMessageSource resourceBundleMessageSource;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApiResult<String> registration(RegistrationDTO registrationDTO, LanguageEnum language) {
        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(registrationDTO.getPhoneNumber());
        if (optional.isPresent()) {
            log.warn("Email already exists email => {}", registrationDTO.getPhoneNumber());
            String message = resourceBundleMessageSource.getMessage("email.exists", null, new Locale(language.name()));
            throw new AppBadException(message);
        }
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            log.warn("Password does not match confirm password => {}", registrationDTO.getConfirmPassword());
            String message = resourceBundleMessageSource.getMessage("password.not.match", null, new Locale(language.name()));
        }

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setFullName(profileEntity.getFullName());
        profileEntity.setPhone(registrationDTO.getPhoneNumber());
//        profileEntity.setPassword();
        return ApiResult.successResponse();
    }
}
