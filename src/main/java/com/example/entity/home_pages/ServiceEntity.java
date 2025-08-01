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

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "salon_id",insertable = false,updatable = false, nullable = false)
    private SalonEntity salon;
    @Column(name = "salon_id")
    private Long salonId;

    @ManyToOne
    @JoinColumn(name = "category_id",insertable = false,updatable = false)
    private Category category;
    @Column(name = "category_id")
    private Long categoryId;


}
