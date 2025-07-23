package com.example.dto.profile;

import com.example.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    @NotBlank(message = "fullName required")
    private String fullName;
    @NotBlank(message = "phone required")
    @Size(min = 9, max = 13, message = "phoneNumber not valid")
    private String phone;
    private List<ProfileRole> role;
    private String jwt;
    private String refreshToken;
}
