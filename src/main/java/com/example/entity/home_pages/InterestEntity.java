package com.example.entity.home_pages;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "interest")
public class InterestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String iconUrl;
    private String description;
}