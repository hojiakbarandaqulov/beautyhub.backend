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

    @ManyToOne
    private ProfileEntity profile;

    @ManyToOne
    private SalonEntity salon;

    @ManyToOne
    private ServiceEntity service;

    @ManyToOne
    private Master master;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String specialRequests;
    private Integer earnedPoints;
    private Double paidAmount;
    private String paymentMethod;
}