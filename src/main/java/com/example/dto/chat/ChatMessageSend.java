package com.example.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageSend {
    @NotNull(message = "recipientId required")
    private Long recipientId;
    @NotNull(message = "content required")
    private String content;
}
