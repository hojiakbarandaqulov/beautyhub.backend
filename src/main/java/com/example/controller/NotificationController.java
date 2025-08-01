package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.notification.NotificationCreateDto;
import com.example.dto.notification.NotificationDto;
import com.example.enums.LanguageEnum;
import com.example.service.NotificationService;
import com.example.util.SpringSecurityUtil;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MASTER', 'ROLE_SALON_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/list")
    public ApiResult<List<NotificationDto>> getUserNotifications() {
        Long profileId = SpringSecurityUtil.getProfileId();
        return notificationService.getUserNotifications(profileId);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MASTER', 'ROLE_SALON_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/me/unread-count")
    public ApiResult<Long> getUnreadCount() {
        Long profileId = SpringSecurityUtil.getProfileId();
        return notificationService.getUnreadCount(profileId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SALON_MANAGER', 'ROLE_MASTER')")
    @PostMapping("/profile/{profileId}")
    public ApiResult<NotificationCreateDto> createNotification(@RequestBody @Valid NotificationCreateDto dto, @PathVariable Long profileId) {
        return notificationService.createNotification(dto, profileId);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MASTER', 'ROLE_SALON_MANAGER', 'ROLE_ADMIN')")
    @PutMapping("/read/{id}")
    public ApiResult<Boolean> markAsRead(@PathVariable Long id, @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        return notificationService.markAsRead(id, language);
    }
}