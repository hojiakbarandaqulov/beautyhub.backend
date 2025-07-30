package com.example.repository;

import com.example.entity.home_pages.Master;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface MasterRepository extends JpaRepository<Master, Long> {
    List<Master> findBySalonId(Long salonId);
}
