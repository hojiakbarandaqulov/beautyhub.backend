package com.example.repository;

import com.example.entity.home_pages.Review;
import com.example.entity.home_pages.SalonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.salon.id = :salonId")
    Double findAverageRatingBySalonId(@Param("salonId") Long salonId);

    List<Review> findBySalonId(Long salonId);
}
