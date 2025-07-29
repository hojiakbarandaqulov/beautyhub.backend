package com.example.entity.home_pages;

import com.example.entity.ProfileEntity;
import com.example.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;


@Entity
@Table(name = "notifications")
@Data
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProfileEntity user;

    private String title;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;
    private Long relatedId;
    private String relatedType;
}