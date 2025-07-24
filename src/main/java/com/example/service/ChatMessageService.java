package com.example.service;

import com.example.dto.chat.ChatMessageDTO;
import com.example.entity.ChatMessageEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.MessageStatus;
import com.example.repository.ChatMessageRepository;
import com.example.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public ChatMessageEntity save(ChatMessageEntity message) {
        return chatMessageRepository.save(message);
    }

    @Transactional
    public Page<ChatMessageEntity> getChatHistory(Long senderId, Long recipientId, Pageable pageable) {
        return chatMessageRepository.findBySenderIdAndRecipientIdOrRecipientIdAndSenderId(
                senderId, recipientId, senderId, recipientId, pageable);
    }

    @Transactional
    public void markAsRead(Long messageId) {
        chatMessageRepository.findById(messageId).ifPresent(message -> {
            message.setIsRead(true);
            message.setStatus(MessageStatus.READ);
            chatMessageRepository.save(message);
        });
    }

    @Transactional
    public List<ProfileEntity> getRecentChatPartners(Long profileId) {
        return chatMessageRepository.findRecentChatPartners(profileId);
    }
}
