package com.example.entity.home_pages;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admin_apps_salon")
public class AdminAppsSalonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 255)
    private String address;

    @Column(length = 20)
    private String phone;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "subscription_active", nullable = false)
    private boolean subscriptionActive=false;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified=false;

    @Column(name = "discount_percentage")
    private Integer discountPercentage; // Chegirma foizi (masalan, 10, 20, 30%)

    @Column(name = "is_outdoor",nullable = false)
    private boolean isOutdoor=false;

    @Column(name = "is_for_kids",nullable = false)
    private boolean isForKids=false;

    @Column(name = "latitude", nullable = true)
    private Double latitude;

    @Column(name = "longitude", nullable = true)
    private Double longitude;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "salon_category_mapping",
            joinColumns = @JoinColumn(name = "salon_id"), // Bu jadvaldagi SalonEntity ID ustuni
            inverseJoinColumns = @JoinColumn(name = "category_id") // Bu jadvaldagi CategoryEntity ID ustuni
    )
    private Set<Category> categories;

    @OneToMany(mappedBy = "salon", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ServiceEntity> services;
}
