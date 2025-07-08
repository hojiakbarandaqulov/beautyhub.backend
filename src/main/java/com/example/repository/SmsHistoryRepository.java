package com.example.repository;

import com.example.entity.SmsHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface SmsHistoryRepository extends JpaRepository<SmsHistoryEntity,String> {
    Long countByPhoneAndCreatedDateBetween(String phone, LocalDateTime localDateTime, LocalDateTime now);
}
