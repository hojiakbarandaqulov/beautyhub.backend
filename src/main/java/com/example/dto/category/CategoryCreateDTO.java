package com.example.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryCreateDTO {
    @NotBlank(message = "nameUz required")
    private String nameUz;
    @NotBlank(message = "nameEn required")
    private String nameEn;
    @NotBlank(message = "nameRu required")
    private String nameRu;
    @NotBlank(message = "iconUrl required")
    private String iconUrl;
}
