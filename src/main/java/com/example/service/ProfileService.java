package com.example.service;

import com.example.dto.RegistrationDTO;
import com.example.dto.base.ApiResult;
import com.example.entity.ProfileEntity;
import com.example.exp.AppBadException;
import com.example.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ResourceBundleMessageSource resourceBundleMessageSource;

}
