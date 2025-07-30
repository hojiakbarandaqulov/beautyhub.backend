package com.example.repository;

import com.example.entity.home_pages.BookingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    Page<BookingEntity> findByProfileId(Long userId, Pageable pageable);

    Optional<BookingEntity> findByIdAndProfileId(Long id, Long userId);


    @Query("SELECT b FROM BookingEntity b WHERE " +
            "b.master.id = :masterId AND " +
            "(b.startTime < :end AND b.endTime > :start) AND " +
            "b.status <> com.example.enums.BookingStatus.CANCELLED")
    List<BookingEntity> findByMasterAndTimeRange(
            @Param("masterId") Long masterId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
