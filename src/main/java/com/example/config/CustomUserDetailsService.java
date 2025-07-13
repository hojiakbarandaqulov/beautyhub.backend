package com.example.config;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.repository.ProfileRepository;
import com.example.repository.ProfileRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;
    private final ProfileRoleRepository profileRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(username);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }
        ProfileEntity profile = optional.get();
        List<ProfileRole> roleList=profileRoleRepository.getAllRolesListByProfileId(profile.getId());
        return new CustomUserDetails(profile,roleList);
    }
}
