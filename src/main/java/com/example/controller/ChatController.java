package com.example.controller;

import com.example.dto.base.ApiResponse;
import com.example.dto.chat.ChatMessageDTO;
import com.example.dto.chat.ChatMessageSend;
import com.example.entity.AttachEntity;
import com.example.entity.ChatMessageEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.MessageType;
import com.example.mapper.ChatMessageMapper;
import com.example.service.AttachService;
import com.example.service.ChatMessageService;
import com.example.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ProfileService profileService;
    private final ChatMessageMapper chatMessageMapper;
    private final AttachService attachService;

    @MessageMapping("/send")
    @SendTo("/queue/chat/messages")
    public ResponseEntity<ApiResponse<?>> sendMessage(@Payload ChatMessageSend chatMessageDTO, Principal principal) {
        ProfileEntity sender = profileService.findByPhone(principal.getName());
        ProfileEntity recipient = profileService.findById(chatMessageDTO.getRecipientId());

        ChatMessageEntity message = new ChatMessageEntity();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setMessageType(chatMessageDTO.getType());
        message.setSentTime(LocalDateTime.now());
        message.setIsRead(false);

        if (chatMessageDTO.getType() == MessageType.IMAGE) {
            if (chatMessageDTO.getImageId() != null && !chatMessageDTO.getImageId().isEmpty()) {
                Optional<AttachEntity> attachOpt = attachService.getImage(chatMessageDTO.getImageId());
                if (attachOpt.isPresent()) {
                    message.setAttaches(List.of(attachOpt.get()));
                    message.setContent(chatMessageDTO.getContent());
                } else {
                    return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Image not found", null));
                }
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Image ID is missing", null));
            }
        } else {
            message.setContent(chatMessageDTO.getContent());
        }
        ChatMessageEntity savedMessage = chatMessageService.save(message);
        ChatMessageDTO dto = chatMessageMapper.toDto(savedMessage);
        messagingTemplate.convertAndSendToUser(sender.getPhone(), "/queue/messages", dto);
        messagingTemplate.convertAndSendToUser(recipient.getPhone(), "/queue/messages", dto);

        return ResponseEntity.ok(new ApiResponse<>(true, "success", savedMessage.getId()));
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<?>> sendMessageRest(@RequestBody @Valid ChatMessageSend messageDTO,
                                                          Principal principal) {
        ProfileEntity sender = profileService.findByPhone(principal.getName());
        ProfileEntity recipient = profileService.findById(messageDTO.getRecipientId());

        ChatMessageEntity message = new ChatMessageEntity();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setMessageType(messageDTO.getType());
        message.setSentTime(LocalDateTime.now());
        message.setIsRead(false);

        if (messageDTO.getType() == MessageType.IMAGE) {
            if (messageDTO.getImageId() != null && !messageDTO.getImageId().isEmpty()) {
                Optional<AttachEntity> attachOpt = attachService.getImage(messageDTO.getImageId());
                if (attachOpt.isPresent()) {
                    message.setAttaches(List.of(attachOpt.get()));
                    message.setContent(messageDTO.getContent());
                } else {
                    return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Image not found", null));
                }
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Image ID is missing", null));
            }
        } else {
            message.setContent(messageDTO.getContent());
        }
        ChatMessageEntity savedMessage = chatMessageService.save(message);
        ChatMessageDTO dto = chatMessageMapper.toDto(savedMessage);
        messagingTemplate.convertAndSendToUser(sender.getPhone(), "/queue/messages", dto);
        messagingTemplate.convertAndSendToUser(recipient.getPhone(), "/queue/messages", dto);

        return ResponseEntity.ok(new ApiResponse<>(true, "success", savedMessage.getId()));
    }

    @GetMapping("/history/{recipientId}")
    public ResponseEntity<ApiResponse<Page<ChatMessageDTO>>> getChatHistory(
            @PathVariable Long recipientId,
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        ApiResponse<Page<ChatMessageDTO>> response = chatMessageService.getChatHistory(recipientId, principal, page - 1, size);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/mark-as-read/{messageId}")
    public ResponseEntity<ApiResponse<Boolean>> markAsRead(@PathVariable Long messageId) {
        ApiResponse<Boolean> booleanApiResponse = chatMessageService.markAsRead(messageId);
        return ResponseEntity.ok(booleanApiResponse);
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