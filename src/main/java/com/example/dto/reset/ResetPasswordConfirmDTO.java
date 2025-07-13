package com.example.dto.reset;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResetPasswordConfirmDTO {

    @NotBlank(message = "phone required")
    private String phone;
    @NotBlank(message = "password required")
    private String password;
    @NotBlank(message = "confirmPassword required")
    private String confirmPassword;

}
