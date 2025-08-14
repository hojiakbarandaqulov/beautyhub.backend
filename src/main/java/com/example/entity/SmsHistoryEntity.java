package com.example.entity;

import com.example.enums.LanguageEnum;
import com.example.enums.SmsType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sms_history")
public class SmsHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "code")
    private String code;

    @Column(name = "message")
    private String message;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "sms_type")
    private SmsType smsType;
    @Column(name = "attempt_count")
    private Integer attemptCount;
}
