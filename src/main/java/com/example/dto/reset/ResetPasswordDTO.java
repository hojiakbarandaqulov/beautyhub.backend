package com.example.dto.reset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDTO {

    @Size(min = 9, max = 13, message = "phoneNumber not valid")
    @NotBlank(message = "phone required")
    private String phone;
}
