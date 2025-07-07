package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationDTO {
    @NotBlank(message = "fullName required")
    private String fullName;

    @NotBlank(message = "phoneNumber required")
    private String phoneNumber;

    @Length(min = 8, max = 20, message = "Parol uzunligi 8 dan kam bo'lmasligi kerak!")
    @NotBlank(message ="password required")
    private String password;

    @Length(min = 8, max = 20, message = "Parol uzunligi 8 dan kam bo'lmasligi kerak!")
    @NotBlank(message = "confirmPassword required")
    private String confirmPassword;

}
