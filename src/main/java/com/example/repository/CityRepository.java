package com.example.repository;

import com.example.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<CityEntity,Long> {



    @Query("SELECT c FROM CityEntity c WHERE " +
            "LOWER(c.nameUz) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.nameRu) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.nameEn) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<CityEntity> fullTextSearch(@Param("query") String query);
}
