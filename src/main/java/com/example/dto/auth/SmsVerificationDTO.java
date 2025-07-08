package com.example.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsVerificationDTO {
    @NotBlank(message = "phone required")
    @Size(min = 9, max = 13, message = "phoneNumber not valid")
    private  String phone;
    @NotBlank(message = "code required")
    private String code;
}
