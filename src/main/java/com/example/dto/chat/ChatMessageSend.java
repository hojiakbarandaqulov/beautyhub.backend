package com.example.dto.chat;

import com.example.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageSend {
    @NotNull
    private Long recipientId;
    private String content;
    private MessageType type = MessageType.CHAT;
    private String imageId;
}
