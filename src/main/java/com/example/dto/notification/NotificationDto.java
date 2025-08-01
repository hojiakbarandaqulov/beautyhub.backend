package com.example.dto.notification;

import com.example.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private String title;
    private String content;
    private boolean read;
    private LocalDateTime createdAt;
}