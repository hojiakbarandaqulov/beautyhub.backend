package com.example.service;

import com.example.entity.ProfileRoleEntity;
import com.example.enums.ProfileRole;
import com.example.repository.ProfileRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ProfileRoleService {
    private final ProfileRoleRepository profileRoleRepository;

    public ProfileRoleService(ProfileRoleRepository profileRoleRepository) {
        this.profileRoleRepository = profileRoleRepository;
    }

    public void create(Long profileId, ProfileRole role) {
        ProfileRoleEntity profileRoleEntity = new ProfileRoleEntity();
        profileRoleEntity.setProfileId(profileId);
        profileRoleEntity.setRoles(role);
        profileRoleEntity.setCreatedDate(LocalDateTime.now());
        profileRoleRepository.save(profileRoleEntity);
    }



    public void deleteRoles(Long profileId) {
        profileRoleRepository.deleteByProfileId(profileId);
    }
}
