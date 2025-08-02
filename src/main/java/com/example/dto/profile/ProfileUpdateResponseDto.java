package com.example.dto.profile;

import com.example.enums.ProfileRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ProfileUpdateResponseDto {
    private Long id;
    private String fullName;
    @NotBlank(message = "phone required")
    private String city;
    private Boolean notifications;
    private String photoUrl;
    private List<ProfileRole> role;
}
