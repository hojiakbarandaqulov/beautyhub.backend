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

    @ManyToOne(fetch = FetchType.LAZY) // Til ma'lumotlari faqat kerak bo'lganda yuklansin
    @JoinColumn(name = "app_language_code") // Ma'lumotlar bazasida 'app_language_code' ustuni bo'ladi (FK)
    private LanguageEntity appLanguage; // Foydalanuvchi tanlagan til obyekti

    // Shaharni saqlash uchun bog'lanish (oldin berilgan)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityEntity city;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_type")
    private SmsType smsType;
    @Column(name = "attempt_count")
    private Integer attemptCount;
}
