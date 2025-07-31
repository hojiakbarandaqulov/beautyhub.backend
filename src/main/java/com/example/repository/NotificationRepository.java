package com.example.repository;

import com.example.entity.home_pages.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByProfileIdOrderByCreatedAtDesc(Long userId);

    Long countByProfileIdAndReadIsFalse(Long profileId);

}
