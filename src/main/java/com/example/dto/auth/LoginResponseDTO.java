package com.example.dto.auth;

import com.example.enums.ProfileRole;
import lombok.Data;

import java.util.List;

@Data
public class LoginResponseDTO {
    private List<ProfileRole> role;
    private String jwt;
    private String refreshToken;
}
