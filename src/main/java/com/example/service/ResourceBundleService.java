package com.example.service;

import com.example.enums.GeneralStatus;
import com.example.enums.LanguageEnum;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Service
public class ResourceBundleService {
    private final ResourceBundleMessageSource resourceBundle;

    public ResourceBundleService(ResourceBundleMessageSource resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public String getMessage(String code, LanguageEnum lang) {
        return resourceBundle.getMessage(code,null, new Locale(lang.name()));
    }

    public String getMessage(String code, String lang) {
        return resourceBundle.getMessage(code,null, new Locale(lang));
    }

}
