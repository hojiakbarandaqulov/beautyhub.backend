package com.example.repository;

import com.example.entity.SmsHistoryEntity;
import com.example.enums.SmsType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SmsHistoryRepository extends JpaRepository<SmsHistoryEntity,String> {
    Long countByPhoneAndCreatedDateBetween(String phone, LocalDateTime localDateTime, LocalDateTime now);


    @Modifying
    @Transactional
    @Query("UPDATE SmsHistoryEntity s SET s.smsType = ?2 WHERE s.id = ?1")
    void updateStatus(String id, SmsType smsType);

    Optional<SmsHistoryEntity> findTop1ByPhoneOrderByCreatedDateDesc(String phone);
}
