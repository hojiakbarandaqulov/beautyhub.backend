package com.example.dto.notification;

import com.example.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDto {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private LocalDateTime createdAt;
    private boolean isRead;
    private Long relatedId;
    private String relatedType;
}
