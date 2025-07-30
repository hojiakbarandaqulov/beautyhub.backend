package com.example.entity.home_pages;

import com.example.entity.AttachEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = "masters")
public class Master {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String specialization;

    @ManyToOne
    @JoinColumn(name = "photo_id",insertable = false, updatable = false)
    private AttachEntity photo;
    @Column(name = "photo_id")
    private String photoId;

    @Column(nullable = false)
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "salon_id", nullable = false)
    private SalonEntity salon;

    @ManyToMany
    @JoinTable(
            name = "master_services",
            joinColumns = @JoinColumn(name = "master_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<ServiceEntity> services = new HashSet<>();

}