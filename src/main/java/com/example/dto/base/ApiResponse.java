package com.example.dto.base;

import com.example.entity.ChatMessageEntity;
import com.example.enums.LanguageEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private Long messageId;

    public ApiResponse(boolean success, String message, Long messageId) {
        this.success = success;
        this.message = message;
        this.messageId = messageId;
    }

}