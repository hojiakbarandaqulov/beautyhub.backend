package com.example.dto.root;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RootDTO {
    @NotNull
    private Long id;
    @NotBlank(message = "name required")
    private String name;
    @NotBlank(message = "with required")
    private Double width;
    @NotNull(message = "height required")
    private Double height;
    @NotBlank(message = "description")
    private String description;
}