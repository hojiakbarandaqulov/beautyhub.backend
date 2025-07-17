package com.example.repository;

import com.example.entity.ProfileEntity;
import com.example.enums.GeneralStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    Optional<ProfileEntity> findByPhoneAndVisibleTrue(String phone);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set status=?2 where id=?1")
    void changeStatus(Long id , GeneralStatus visible);

    @Modifying
    @Transactional
    @Query("update ProfileEntity set password=?2 where id =?1")
    void updatePassword(Long id, String password);

    Optional<ProfileEntity> findByIdAndVisibleTrue(Long id);

    @Modifying
    @Transactional
    @Query("update ProfileEntity set photoId=?2 where id=?1")
    void updateProfilePhoto(Long profileId, String photoId);
}
