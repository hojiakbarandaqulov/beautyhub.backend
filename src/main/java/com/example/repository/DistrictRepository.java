package com.example.repository;

import com.example.entity.DistrictEntity;
import com.example.mapper.DistrictMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistrictRepository extends JpaRepository<DistrictEntity, Long> {

    @Query(value = "SELECT id, " +
            "  CASE :language " +
            "    WHEN 'uz' THEN name_uz " +
            "    WHEN 'en' THEN name_en " +
            "    WHEN 'ru' THEN name_ru " +
            "  END as name " +
            "FROM districts " +
            "WHERE (:id IS NULL OR id = :id) " +
            "ORDER BY id ASC",
            nativeQuery = true)
    List<DistrictMapper> findAllByLanguage(
            @Param("language") String language,
            @Param("id") Long id);
}
