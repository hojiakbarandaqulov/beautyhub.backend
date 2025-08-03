package com.example.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileUpdateDto {
    private String fullName;
    private Boolean notifications;
    private String photoId;
}
