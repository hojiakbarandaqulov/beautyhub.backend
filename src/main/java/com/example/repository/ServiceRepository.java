package com.example.repository;

import com.example.entity.home_pages.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    List<ServiceEntity> findBySalonId(Long salonId);

    @Query("SELECT s FROM ServiceEntity s WHERE s.salon.id = :salonId AND LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<ServiceEntity> searchBySalonAndName(@Param("salonId") Long salonId, @Param("search") String search);

    boolean existsBySalonIdAndNameIgnoreCase(Long salonId, String name);
}
