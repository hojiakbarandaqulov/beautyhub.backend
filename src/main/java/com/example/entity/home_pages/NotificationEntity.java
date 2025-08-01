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
    @JoinColumn(name = "profile_id",insertable = false, updatable = false)
    private ProfileEntity profile;
    @Column(name = "profile_id")
    private Long profileId;

    private String title;
    private String content;
    private Boolean read; // o‘qilgan/o‘qilmagan
    private LocalDateTime createdAt;
}