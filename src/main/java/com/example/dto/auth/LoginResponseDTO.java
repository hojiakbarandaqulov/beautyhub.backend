package com.example.dto.auth;

import com.example.enums.ProfileRole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponseDTO {
    private List<ProfileRole> role;
    private String jwt;
    private String refreshToken;
}
