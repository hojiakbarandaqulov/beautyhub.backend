package com.example.entity;

import com.example.enums.GeneralStatus;
import com.example.enums.LanguageEnum;
import com.example.enums.ProfileRole;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "profile")
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone")
    private String phone;
    @Column(name = "temp_phone")
    private String tempPhone;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GeneralStatus status;

    @Column(name = "photo_id")
    private String photoId;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "photo_id",insertable=false,updatable=false)
    private AttachEntity photo;

    @Column(name = "notifications_enabled")
    private Boolean notificationsEnabled = Boolean.TRUE; // "Уведомления"
    @Column(name = "dark_theme_enabled")
    private Boolean darkThemeEnabled = Boolean.FALSE;

    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;

    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private LanguageEnum language=LanguageEnum.ru;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "city_id",insertable=false,updatable=false)
    private CityEntity city;
    
   /* @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CardEntity> cards;*/

}
