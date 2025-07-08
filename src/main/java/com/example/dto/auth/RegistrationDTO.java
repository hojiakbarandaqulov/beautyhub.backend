package com.example.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationDTO {
    @NotBlank(message = "fullName required")
    private String fullName;

    @NotBlank(message = "phoneNumber required")
    @Size(min = 9, max = 13, message = "phoneNumber not valid")
    private String phone;

    @Size(min = 6, message = "Password size min 6 chars.")
    @NotBlank(message ="password required")
    private String password;

    @Length(min = 8, max = 20, message = "Parol uzunligi 8 dan kam bo'lmasligi kerak!")
    @NotBlank(message = "confirmPassword required")
    private String confirmPassword;

}
