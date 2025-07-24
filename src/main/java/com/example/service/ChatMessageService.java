package com.example.service;

import com.example.dto.base.ApiResponse;
import com.example.dto.chat.ChatMessageDTO;
import com.example.entity.ChatMessageEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.MessageStatus;
import com.example.mapper.ChatMessageMapper;
import com.example.repository.ChatMessageRepository;
import com.example.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ProfileService profileService;
    private final ChatMessageMapper chatMessageMapper;

    @Transactional
    public ChatMessageEntity save(ChatMessageEntity message) {
        return chatMessageRepository.save(message);
    }

    public ApiResponse<Page<ChatMessageDTO>> getChatHistory(Long recipientId,Principal principal, int page, int size) {
        ProfileEntity sender = profileService.findByPhone(principal.getName());
        ProfileEntity recipient = profileService.findById(recipientId);
        PageImpl<ChatMessageEntity> messages = chatMessageRepository.findChatHistoryBetweenUsers(
                sender.getId(),
                recipient.getId(),
                PageRequest.of(page, size, Sort.by("sentTime").descending())
        );
        Page<ChatMessageDTO> dtoPage = messages.map(chatMessageMapper::toDto);

        return ApiResponse.success(dtoPage);
    }

    @Transactional
    public ApiResponse<Boolean> markAsRead(Long messageId) {
        Optional<ChatMessageEntity> byMessageId = chatMessageRepository.getByMessageId(messageId);
        if (byMessageId.isPresent()) {
            ChatMessageEntity message = byMessageId.get();
            message.setIsRead(true);
            message.setStatus(MessageStatus.READ);
            chatMessageRepository.save(message);
            return ApiResponse.success(true);
        }
        return ApiResponse.success(false);
    }

    @Transactional
    public List<ProfileEntity> getRecentChatPartners(Long profileId) {
        return chatMessageRepository.findRecentChatPartners(profileId);
    }
}
