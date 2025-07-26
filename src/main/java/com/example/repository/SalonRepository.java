package com.example.repository;


import com.example.entity.home_pages.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface SalonRepository extends JpaRepository<Salon, Long> {

   /* @Query(value = "SELECT s.*, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
            "cos(radians(s.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(s.latitude)))) AS distance " +
            "FROM salons s " +
            "HAVING distance < :radius " +
            "ORDER BY distance",
            nativeQuery = true)
    List<Salon> findNearbySalons(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radiusKm);*/

    @Query(value = "SELECT * FROM salons s " +
            "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
            "cos(radians(s.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(s.latitude)))) < :radiusKm " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
            "cos(radians(s.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(s.latitude))))",
            nativeQuery = true)
    List<Salon> findNearbySalons(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radiusKm") Double radiusKm);


    @Query("SELECT s FROM Salon s WHERE " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.address) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Salon> searchSalons(@Param("query") String query);

    @Query("SELECT DISTINCT s FROM Salon s JOIN s.services serv " +
            "WHERE serv.category.id = :category")
    List<Salon> findByCategory(@Param("category") Long category);

}
