package com.example.dto.profile;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfileUpdatePhoneDTO {
    @NotNull(message = "phone required")
    private String phone;
}
