package com.example.util;

import com.example.config.CustomUserDetail;
import com.example.entity.ProfileEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtil {

    public static CustomUserDetail getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        return user;
    }

    public static ProfileEntity getProfileId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetail) {
            CustomUserDetail user = (CustomUserDetail) principal;
            return user.getProfile();
        } else if (principal instanceof String) {
            String username = (String) principal;
        }
        return null;
    }
}
