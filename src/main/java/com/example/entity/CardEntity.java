package com.example.entity;

import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name = "card")
@Entity
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    private String number;

    @Column(nullable = false)
    private String holder;

    @Column(nullable = false)
    private Double balance = 0.0;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

}
