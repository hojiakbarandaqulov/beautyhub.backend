package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.notification.NotificationCreateDto;
import com.example.dto.notification.NotificationDto;
import com.example.enums.LanguageEnum;
import com.example.service.NotificationService;
import com.example.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Bildirishnoma", description = "Bildirishnomalar bilan ishlash: olish, o'qilgan deb belgilash, yaratish va sanash")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MASTER', 'ROLE_SALON_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/list")
    @Operation(
            summary = "Foydalanuvchi bildirishnomalari ro'yxati",
            description = "Kirish qilgan foydalanuvchining barcha bildirishnomalari ro'yxatini qaytaradi. Har bir bildirishnoma haqida ma'lumotlarni olasiz."
    )
    public ApiResult<List<NotificationDto>> getUserNotifications() {
        Long profileId = SpringSecurityUtil.getProfileId();
        return notificationService.getUserNotifications(profileId);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MASTER', 'ROLE_SALON_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/me/unread-count")
    @Operation(
            summary = "O'qilmagan bildirishnomalar soni",
            description = "Foydalanuvchining o'qilmagan bildirishnomalari sonini qaytaradi. Statistika uchun ishlatiladi."
    )
    public ApiResult<Long> getUnreadCount() {
        Long profileId = SpringSecurityUtil.getProfileId();
        return notificationService.getUnreadCount(profileId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SALON_MANAGER', 'ROLE_MASTER')")
    @PostMapping("/profile/{profileId}")
    @Operation(
            summary = "Yangi bildirishnoma yaratish",
            description = "Berilgan profileId uchun yangi bildirishnoma yaratadi. Faqat admin, salon manager va masterlar foydalanadi."
    )
    public ApiResult<NotificationCreateDto> createNotification(@RequestBody @Valid NotificationCreateDto dto, @PathVariable Long profileId) {
        return notificationService.createNotification(dto, profileId);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MASTER', 'ROLE_SALON_MANAGER', 'ROLE_ADMIN')")
    @PutMapping("/read/{id}")
    @Operation(
            summary = "Bildirishnomani o'qilgan deb belgilash",
            description = "Berilgan id bo'yicha foydalanuvchi bildirishnomasini o'qilgan deb belgilaydi. O'qilmaganlar soni kamayadi."
    )
    public ApiResult<Boolean> markAsRead(@PathVariable Long id, @RequestHeader(value = "Accept-Language", defaultValue = "ru") LanguageEnum language) {
        return notificationService.markAsRead(id, language);
    }
}