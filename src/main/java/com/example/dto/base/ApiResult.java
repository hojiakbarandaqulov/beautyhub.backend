package com.example.dto.base;

import com.example.enums.LanguageEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Locale;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> implements Serializable {

    private  Boolean success;

    private String message;

    private T data;

    private ApiResult(String s, Boolean success, Locale locale) {
        this.success = success;
    }

    private ApiResult(T data, Boolean success) {
        this.data = data;
        this.success = success;
    }

    private ApiResult(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }

    public ApiResult(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public ApiResult(T data) {
        this.data = data;
    }

    public <E> ApiResult(E data, Boolean aTrue, String message) {
    }

    public static <E> ApiResult<E> successResponse(String s, E data, Locale locale) {
        return new ApiResult<>(data, Boolean.TRUE);
}

    public static <E> ApiResult<E> successResponse(E data) {
        return new ApiResult<>(data, Boolean.TRUE);
    }

    public static ApiResult<String> successResponse(String message) {
        return new ApiResult<>(message, Boolean.TRUE);
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

}
