package com.example.dto.language;

import com.example.enums.LanguageEnum;
import lombok.Data;

@Data
public class LanguageUpdateDto {
    private LanguageEnum languageCode;
}
