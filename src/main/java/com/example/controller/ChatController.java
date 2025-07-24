package com.example.controller;

import com.example.dto.base.ApiResponse;
import com.example.dto.base.ApiResult;
import com.example.dto.chat.ChatMessageDTO;
import com.example.dto.chat.ChatMessageSend;
import com.example.entity.ChatMessageEntity;
import com.example.entity.ProfileEntity;
import com.example.mapper.ChatMessageMapper;
import com.example.service.ChatMessageService;
import com.example.service.ProfileService;
import com.example.util.SpringSecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ProfileService profileService;
    private final ChatMessageMapper chatMessageMapper;
    // WebSocket orqali xabar yuborish
    @MessageMapping("/send")
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<?>> sendMessage(@RequestBody @Valid ChatMessageSend chatMessageDTO, Principal principal) {
        ProfileEntity sender = profileService.findByPhone(principal.getName());
//        Long profileId = SpringSecurityUtil.getProfileId();
        ProfileEntity recipient = profileService.findById(chatMessageDTO.getRecipientId());

        ChatMessageEntity message = new ChatMessageEntity();
        message.setContent(chatMessageDTO.getContent());
        message.setSender(sender);
        message.setRecipient(recipient);

        ChatMessageEntity savedMessage = chatMessageService.save(message);
//        ChatMessageDTO dto = ChatMessageMapper.toDto(savedMessage);

        // Yuboruvchiga tasdiq
        messagingTemplate.convertAndSendToUser(
                sender.getPhone(),
                "/queue/messages",
                savedMessage
        );

        // Qabul qiluvchiga xabar
        messagingTemplate.convertAndSendToUser(
                recipient.getPhone(),
                "/queue/messages",
                savedMessage
        );
        return ResponseEntity.ok(new ApiResponse<>(true,"success",savedMessage.getId()));
    }

    // REST orqali chat tarixi
    @GetMapping("/history/{recipientId}")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory(
            @PathVariable Long recipientId,
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        ProfileEntity sender = profileService.findByPhone(principal.getName());
        ProfileEntity recipient = profileService.findById(recipientId);

        Page<ChatMessageEntity> messages = chatMessageService.getChatHistory(
                sender.getId(),
                recipient.getId(),
                PageRequest.of(page, size, Sort.by("sentTime").descending())
        );

        List<ChatMessageDTO> dtos = messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // Xabarlarni "o'qildi" deb belgilash
    @PutMapping("/mark-as-read/{messageId}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long messageId) {
        chatMessageService.markAsRead(messageId);
        return ResponseEntity.ok().build();
    }

    private ChatMessageDTO convertToDto(ChatMessageEntity message) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setSenderId(message.getSender().getId());
        dto.setRecipientId(message.getRecipient().getId());
        dto.setSentTime(message.getSentTime());
        dto.setRead(message.getIsRead());
        return dto;
    }
}