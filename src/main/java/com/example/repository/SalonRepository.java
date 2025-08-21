package com.example.repository;


import com.example.entity.home_pages.AdminAppsSalonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface SalonRepository extends JpaRepository<AdminAppsSalonEntity, Long> {

    @Query(value = "SELECT * FROM admin_apps_salon s " +
            "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
            "cos(radians(s.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(s.latitude)))) < :radiusKm " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
            "cos(radians(s.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(s.latitude))))",
            nativeQuery = true)
    List<AdminAppsSalonEntity> findNearbySalons(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radiusKm") Double radiusKm);


    @Query("SELECT s FROM AdminAppsSalonEntity s WHERE " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.address) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<AdminAppsSalonEntity> searchSalons(@Param("query") String query);

    @Query("SELECT DISTINCT s FROM AdminAppsSalonEntity s JOIN s.services serv " +
            "WHERE serv.category.id = :category")
    List<AdminAppsSalonEntity> findByCategory(@Param("category") Long category);

    @Query("SELECT DISTINCT s FROM AdminAppsSalonEntity s " +
            "LEFT JOIN s.categories c " +
            "WHERE (LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.address) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:categoryId IS NULL OR c.id = :categoryId)")
    Page<AdminAppsSalonEntity> searchSalonsByQueryAndCategory(
            @Param("query") String query,
            @Param("categoryId") Long categoryId,
            Pageable pageable);
}
