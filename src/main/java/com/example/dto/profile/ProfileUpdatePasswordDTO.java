package com.example.dto.profile;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfileUpdatePasswordDTO {
    @NotNull(message = "newPassword required")
    private String newPassword;

    @NotNull(message = "confirmPassword required")
    private String confirmPassword;
}
