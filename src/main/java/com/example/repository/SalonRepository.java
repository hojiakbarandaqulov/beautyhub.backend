package com.example.repository;


import com.example.entity.home_pages.SalonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface SalonRepository extends JpaRepository<SalonEntity, Long> {

   /* @Query(value = "SELECT * FROM salons s " +
            "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
            "cos(radians(s.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(s.latitude)))) < :radiusKm " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
            "cos(radians(s.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(s.latitude))))",
            nativeQuery = true)
    List<SalonEntity> findNearbySalons(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radiusKm") Double radiusKm);*/


   /* @Query("SELECT s FROM SalonEntity s WHERE " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.address) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<SalonEntity> searchSalons(@Param("query") String query);*/
/*
    @Query("SELECT DISTINCT s FROM SalonEntity s JOIN s.services serv " +
            "WHERE serv.category.id = :category")
    List<SalonEntity> findByCategory(@Param("category") Long category);*/

}
