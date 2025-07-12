package com.example.repository;

import com.example.entity.AttachEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AttachRepository extends JpaRepository<AttachEntity, String> {

    @Transactional
    @Modifying
    @Query("update  AttachEntity  set visible= false where  id=?1")
    void delete(String id);
}
