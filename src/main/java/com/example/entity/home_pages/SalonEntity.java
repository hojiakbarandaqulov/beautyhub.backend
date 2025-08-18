package com.example.entity.home_pages;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admin_apps_salon")
public class SalonEntity {
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
    private boolean subscriptionActive;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;
}
