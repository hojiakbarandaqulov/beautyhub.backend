package com.example.entity.home_pages;

import com.example.entity.ProfileEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private Double rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "profile_id",insertable = false, updatable = false, nullable = false)
    private ProfileEntity profile;
    @Column(name = "profile_id")
    private Long profileId;

    @ManyToOne
    @JoinColumn(name = "salon_id",insertable = false,updatable = false, nullable = false)
    private AdminAppsSalonEntity salon;
    @Column(name = "salon_id")
    private Long salonId;
}