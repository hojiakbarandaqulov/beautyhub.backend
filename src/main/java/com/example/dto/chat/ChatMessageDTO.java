package com.example.dto.chat;

import com.example.enums.MessageStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDTO {
    private Long id;
    private String content;
    private Long senderId;
    private Long recipientId;
    private LocalDateTime sentTime;
    private boolean read;
    private MessageStatus status;
}
