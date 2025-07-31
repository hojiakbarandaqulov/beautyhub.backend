package com.example.service;

import com.example.dto.base.ApiResult;
import com.example.dto.notification.NotificationCreateDto;
import com.example.dto.notification.NotificationDto;
import com.example.entity.home_pages.NotificationEntity;
import com.example.enums.LanguageEnum;
import com.example.exp.AppBadException;
import com.example.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ResourceBundleService messagesService;

    public ApiResult<List<NotificationDto>> getUserNotifications(Long profileId) {
        List<NotificationDto> collect = notificationRepository.findByProfileIdOrderByCreatedAtDesc(profileId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ApiResult.successResponse(collect);

    }

    // O'qilmagan notificationlar soni
    public ApiResult<Long> getUnreadCount(Long profileId) {
        Long result = notificationRepository.countByProfileIdAndReadIsFalse(profileId);
        return ApiResult.successResponse(result);
    }

    // Notification yaratish
    public ApiResult<NotificationCreateDto> createNotification(NotificationCreateDto dto, Long profileId) {
        NotificationEntity notification = new NotificationEntity();
        notification.setTitle(dto.getTitle());
        notification.setContent(dto.getContent());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setProfileId(profileId);
        NotificationEntity saved = notificationRepository.save(notification);
        return ApiResult.successResponse(toCreateDto(saved));
    }

    // Notificationni o'qilgan deb belgilash
    public ApiResult<Boolean> markAsRead(Long notificationId, LanguageEnum language) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppBadException(messagesService.getMessage("notification.not.found", language)));
        notification.setRead(true);
        notificationRepository.save(notification);
        return ApiResult.successResponse(true);
    }

    private NotificationDto toDto(NotificationEntity notification) {
        NotificationDto dto = new NotificationDto();
        dto.setTitle(notification.getTitle());
        dto.setContent(notification.getContent());
        dto.setRead(notification.getRead());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }

    private NotificationCreateDto toCreateDto(NotificationEntity saved) {
        NotificationCreateDto notificationCreateDto = new NotificationCreateDto();
        notificationCreateDto.setTitle(saved.getTitle());
        notificationCreateDto.setContent(saved.getContent());
        return notificationCreateDto;
    }

}