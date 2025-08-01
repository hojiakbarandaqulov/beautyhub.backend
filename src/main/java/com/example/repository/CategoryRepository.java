package com.example.repository;

import com.example.entity.home_pages.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameUz(String nameUz);
    boolean existsByNameEn(String nameEn);
    boolean existsByNameRu(String nameRu);}
