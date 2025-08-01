package com.example.dto.category;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private String iconUrl;
}