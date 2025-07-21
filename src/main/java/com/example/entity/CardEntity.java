package com.example.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name = "card")
@Entity
public class CardEntity {
    @Id
    private String token;

    private String maskedNumber;
    private String expire;
    private Long userId;
    private Boolean verified = false;
    private LocalDateTime created = LocalDateTime.now();

}
