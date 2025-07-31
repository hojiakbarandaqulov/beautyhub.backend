package com.example.entity;

import com.example.enums.GeneralStatus;
import com.example.enums.LanguageEnum;
import com.example.enums.ProfileRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "profile")
public class ProfileEntity{
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

    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name = "photo_id",insertable=false,updatable=false)
    private AttachEntity photo;

    @Column(name = "notifications_enabled")
    private Boolean notificationsEnabled = Boolean.TRUE;
    @Column(name = "dark_theme_enabled")
    private Boolean darkThemeEnabled = Boolean.FALSE;

    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;

    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private LanguageEnum language=LanguageEnum.ru;

    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name = "city_id",insertable=false,updatable=false)
    private CityEntity city;

}
