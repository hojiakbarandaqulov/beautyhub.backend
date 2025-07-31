package com.example.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private Long messageId;
    private T data;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.messageId = messageId;
    }

    public ApiResponse(Boolean success,T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setSuccess(true);
        res.setData(data);
        return res;
    }

}