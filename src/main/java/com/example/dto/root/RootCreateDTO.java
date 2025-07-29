package com.example.dto.root;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RootCreateDTO {
    @NotBlank(message = "name required")
    private String name;
    @NotNull(message = "with required")
    private Double width;
    @NotNull(message = "height required")
    private Double height;
    @NotBlank(message = "description")
    private String description;
}