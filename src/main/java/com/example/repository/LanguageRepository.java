package com.example.repository;

import com.example.entity.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {
    List<LanguageEntity> findAllByOrderByOrderNumberAsc();

    Optional<Object> findByCode(String code);
}
