package com.example.dto.profile;

import com.example.enums.ProfileRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ProfileDetailDTO {
    @NotBlank(message = "fullName required")
    private String fullName;
    @NotBlank(message = "phone required")
    @Size(min = 9, max = 13, message = "phoneNumber not valid")
    private String phone;
    private List<ProfileRole> role;

}
