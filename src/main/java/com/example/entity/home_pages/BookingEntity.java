package com.example.entity.home_pages;

import com.example.entity.ProfileEntity;
import com.example.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;
    @Column(name = "profile_id")
    private Long profileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salon_id", insertable = false, updatable = false)
    private AdminAppsSalonEntity salon;
    @Column(name = "salon_id")
    private Long salonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", insertable = false, updatable = false)
    private ServiceEntity service;
    @Column(name = "service_id")
    private Long serviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", insertable = false, updatable = false)
    private Master master;
    @Column(name = "master_id")
    private Long masterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String specialRequests;
    private Integer earnedPoints;
    private Double paidAmount;
    private String paymentMethod;
}