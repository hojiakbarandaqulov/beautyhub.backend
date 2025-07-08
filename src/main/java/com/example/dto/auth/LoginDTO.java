package com.example.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginDTO {
    @NotBlank(message = "phone required")
    private String phone;

    @NotBlank(message = "password required")
    private String password;
}
