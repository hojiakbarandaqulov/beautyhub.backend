package com.example.entity;

import com.example.entity.home_pages.InterestEntity;
import com.example.enums.GenderEnum;
import com.example.enums.GeneralStatus;
import com.example.enums.LanguageEnum;
import com.example.enums.ProfileRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
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

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "temp_phone")
    private String tempPhone;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderEnum gender; // Enum: MALE, FEMALE, OTHER

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GeneralStatus status = GeneralStatus.ACTIVE;

    @Column(name = "photo_id")
    private String photoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private AttachEntity photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityEntity city;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "profile_interest",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private List<InterestEntity> interests;

    @Column(name = "is_premium")
    private Boolean isPremium = false;

    @Column(name = "premium_expiry")
    private LocalDateTime premiumExpiry;

    @Column(name = "bonus_points")
    private Integer bonusPoints = 0;

    @Column(name = "notifications_enabled")
    private Boolean notificationsEnabled = true;

    @Column(name = "dark_theme_enabled")
    private Boolean darkThemeEnabled = false;

    @Column(name = "visible")
    private Boolean visible = true;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private LanguageEnum language = LanguageEnum.ru;

}
