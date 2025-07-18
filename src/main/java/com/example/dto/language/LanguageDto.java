package com.example.dto.language;

import lombok.Data;

@Data
public class LanguageDto {
    private String code;
    private String name;
    private String flagIconUrl;
    private Integer orderNumber;
    private Boolean isSelected;
}
