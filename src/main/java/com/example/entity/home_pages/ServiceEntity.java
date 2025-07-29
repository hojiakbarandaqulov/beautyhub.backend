package com.example.entity.home_pages;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "services")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column
    private Integer duration; // in minutes

    @ManyToOne
    @JoinColumn(name = "salon_id", nullable = false)
    private SalonEntity salon;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
