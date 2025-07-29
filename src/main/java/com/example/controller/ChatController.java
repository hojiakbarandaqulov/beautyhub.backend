package com.example.controller;

import com.example.dto.base.ApiResponse;
import com.example.dto.chat.ChatMessageDTO;
import com.example.dto.chat.ChatMessageSend;
import com.example.entity.ChatMessageEntity;
import com.example.entity.ProfileEntity;
import com.example.mapper.ChatMessageMapper;
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

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ProfileService profileService;
    private final ChatMessageMapper chatMessageMapper;

    @MessageMapping("/send")
    @SendTo("/queue/chat/messages")
    public ResponseEntity<ApiResponse<?>> sendMessage(@Payload @RequestBody @Valid ChatMessageSend chatMessageDTO, Principal principal) {
        ProfileEntity sender = profileService.findByPhone(principal.getName());
        ProfileEntity recipient = profileService.findById(chatMessageDTO.getRecipientId());

        ChatMessageEntity message = new ChatMessageEntity();
        message.setContent(chatMessageDTO.getContent());
        message.setSender(sender);
        message.setRecipient(recipient);

        ChatMessageEntity savedMessage = chatMessageService.save(message);
        ChatMessageDTO dto = chatMessageMapper.toDto(savedMessage);

        messagingTemplate.convertAndSendToUser(
                sender.getPhone(),
                "/queue/messages",
                dto
        );

        messagingTemplate.convertAndSendToUser(
                recipient.getPhone(),
                "/queue/messages",
                dto
        );
        return ResponseEntity.ok(new ApiResponse<>(true,"success",savedMessage.getId()));
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<?>> sendMessageRest(@RequestBody @Valid ChatMessageSend messageDTO,
                                                          Principal principal) {
        Long id = handleSendMessage(messageDTO, principal);
        return ResponseEntity.ok(new ApiResponse<>(true, "success", id));
    }

    @GetMapping("/history/{recipientId}")
    public ResponseEntity<ApiResponse<Page<ChatMessageDTO>>> getChatHistory(
            @PathVariable Long recipientId,
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        ApiResponse<Page<ChatMessageDTO>> response = chatMessageService.getChatHistory(recipientId,principal, page-1, size);
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
    private Long handleSendMessage(ChatMessageSend chatMessageDTO, Principal principal) {
        ProfileEntity sender = profileService.findByPhone(principal.getName());
        ProfileEntity recipient = profileService.findById(chatMessageDTO.getRecipientId());

        ChatMessageEntity message = new ChatMessageEntity();
        message.setContent(chatMessageDTO.getContent());
        message.setSender(sender);
        message.setRecipient(recipient);

        ChatMessageEntity savedMessage = chatMessageService.save(message);
        ChatMessageDTO dto = chatMessageMapper.toDto(savedMessage);

        messagingTemplate.convertAndSendToUser(sender.getPhone(), "/queue/messages", dto);
        messagingTemplate.convertAndSendToUser(recipient.getPhone(), "/queue/messages", dto);

        return savedMessage.getId();
    }
}