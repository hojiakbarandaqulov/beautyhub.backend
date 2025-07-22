package com.example.repository;

import com.example.entity.ProfileRoleEntity;
import com.example.enums.ProfileRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRoleRepository extends JpaRepository<ProfileRoleEntity, Long> {

    @Query("select p.roles from ProfileRoleEntity p where p.profileId=?1")
    List<ProfileRole> getAllRolesListByProfileId(Long profileId);


    @Transactional
    @Modifying
    @Query("DELETE from ProfileRoleEntity p where p.profileId=:profileId")
    void deleteByProfileId(@Param("profileId") Long profileId);

}
