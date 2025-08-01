package com.example.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationCreateDto {
    @NotBlank(message = "title required")
    private String title;
    @NotBlank(message = "content")
    private String content;
}
