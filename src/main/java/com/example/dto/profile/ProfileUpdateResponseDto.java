package com.example.dto.profile;

import com.example.enums.ProfileRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ProfileUpdateResponseDto {
    private Long id;
    private String fullName;
    private Boolean notifications;
    private String photoUrl;
    private List<ProfileRole> role;
}
