package com.example.dto.auth;

import com.example.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RefreshTokenResponseDTO {
    private String jwt;
    private String refreshToken;

    public RefreshTokenResponseDTO(String jwt, String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        this.jwt=jwt;
    }

    public RefreshTokenResponseDTO() {
    }
}
