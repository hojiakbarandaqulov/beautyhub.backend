package com.example.dto.base;

import com.example.dto.city.SearchResultDTO;
import com.example.entity.ChatMessageEntity;
import com.example.enums.LanguageEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> implements Serializable {

    private Boolean success;
    private String errMessage;
    private T data;
    private Map<LanguageEnum, String> messages;

    private ApiResult(String s, Boolean success, Locale locale) {
        this.success = success;
    }

    private ApiResult(T data, Boolean success) {
        this.data = data;
        this.success = success;
    }

    private ApiResult(String errMessage, Boolean success) {
        this.errMessage = errMessage;
        this.success = success;
    }

    public ApiResult(T data, String errMessage) {
        this.data = data;
        this.errMessage = errMessage;
    }

    public ApiResult(T data) {
        this.data = data;
    }

    public <E> ApiResult(E data, Boolean aTrue, String errMessage) {
        this.data = (T) data;
        this.success = aTrue;
        this.errMessage = errMessage;
    }

    public ApiResult(T data, Map<LanguageEnum, String> messages) {
        this.data = data;
        this.messages = messages;
        if (messages != null && !messages.isEmpty()) {
            this.errMessage = messages.get(LanguageEnum.uz); // Default to Uzbek
        }
        this.success = true;
    }

    // New constructor for error with multilingual messages
    public ApiResult(String errorMsg, Map<LanguageEnum, String> messages, Boolean success) {
        this.errMessage = errorMsg;
        this.messages = messages;
        this.success = success;
    }


    // Existing static factory methods remain unchanged
    public static <E> ApiResult<E> successResponse(String s, E data, Locale locale) {
        return new ApiResult<>(data, Boolean.TRUE);
    }

    public static <E> ApiResult<E> successResponse(E data) {
        return new ApiResult<>(data, Boolean.TRUE);
    }

    public static ApiResult<String> successResponse(String errMessage) {
        return new ApiResult<>(errMessage, Boolean.TRUE);
    }

    public static ApiResult<ErrorResponse> errorResponse(String errorMsg) {
        return new ApiResult<>(errorMsg, Boolean.FALSE);
    }

    public static ApiResult<ErrorResponse> errorResponse(String errorMsg, Integer errorCode) {
        return new ApiResult<>(new ErrorResponse(errorMsg, errorCode), Boolean.FALSE);
    }

    public static ApiResult<ErrorResponse> errorResponse(ErrorResponse data) {
        return new ApiResult<>(data, Boolean.FALSE);
    }

    public static <E> ApiResult<E> successResponse(E data, Map<LanguageEnum, String> messages) {
        return new ApiResult<>(data, messages);
    }

    public static ApiResult<ErrorResponse> errorResponse(Map<LanguageEnum, String> messages) {
        String defaultMsg = messages != null ? messages.get(LanguageEnum.uz) : "Error occurred";
        return new ApiResult<>(defaultMsg, messages, Boolean.FALSE);
    }
}