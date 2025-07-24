package com.example.dto.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatPartnerDTO {
    private Long id;
    private String fullName;
    private String phone;
    private String photoUrl;
    private LocalDateTime lastMessageTime;
    private String lastMessagePreview;
    private boolean hasUnreadMessages;
}