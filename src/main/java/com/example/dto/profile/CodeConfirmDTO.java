package com.example.dto.profile;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CodeConfirmDTO {
    @NotNull(message = "code required")
    private String code;
}
