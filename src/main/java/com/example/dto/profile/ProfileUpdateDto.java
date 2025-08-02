package com.example.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileUpdateDto {
    private String fullName;
    @NotBlank(message = "phone required")
    @Size(min = 9, max = 13, message = "phoneNumber not valid")
    private String phone;
    private Long cityId;
    private Boolean notificationsEnabled;
    private String photoId;
}
