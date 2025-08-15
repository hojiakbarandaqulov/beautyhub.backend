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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/chat")
@AllArgsConstructor
@Tag(name = "Chat", description = "Foydalanuvchilar o'rtasida xabar yuborish va tarixini olish uchun APIlar")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ProfileService profileService;
    private final ChatMessageMapper chatMessageMapper;
    private final AttachService attachService;

    @MessageMapping("/send")
    @SendTo("/queue/chat/messages")
    @Operation(
            summary = "Real-time xabar yuborish (WebSocket)",
            description = "Foydalanuvchi real vaqt rejimida boshqa foydalanuvchiga xabar yuboradi. Rasm/xabar/voice yuborish mumkin."
    )
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
                    return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Image not found"));
                }
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Image ID is missing"));
            }
        } else {
            message.setContent(chatMessageDTO.getContent());
        }
        ChatMessageEntity savedMessage = chatMessageService.save(message);
        ChatMessageDTO dto = chatMessageMapper.toDto(savedMessage);
        messagingTemplate.convertAndSendToUser(sender.getPhone(), "/queue/messages", dto);
        messagingTemplate.convertAndSendToUser(recipient.getPhone(), "/queue/messages", dto);

        return ResponseEntity.ok(new ApiResponse<>(true, "success"));
    }

    @PostMapping("/send")
    @Operation(
            summary = "REST orqali xabar yuborish",
            description = "Oddiy HTTP orqali xabar yuboriladi. Xabar turi va kontenti yuboriladi."
    )
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
                    return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Image not found"));
                }
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Image ID is missing"));
            }
        } else {
            message.setContent(messageDTO.getContent());
        }
        ChatMessageEntity savedMessage = chatMessageService.save(message);
        ChatMessageDTO dto = chatMessageMapper.toDto(savedMessage);
        messagingTemplate.convertAndSendToUser(sender.getPhone(), "/queue/messages", dto);
        messagingTemplate.convertAndSendToUser(recipient.getPhone(), "/queue/messages", dto);

        return ResponseEntity.ok(new ApiResponse<>(true, "success"));
    }

    @GetMapping("/history/{recipientId}")
    @Operation(
            summary = "Chat tarixini olish",
            description = "Berilgan recipientId bo'yicha chat tarixini paginatsiya bilan qaytaradi."
    )
    public ResponseEntity<ApiResponse<Page<ChatMessageDTO>>> getChatHistory(
            @PathVariable Long recipientId,
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        ApiResponse<Page<ChatMessageDTO>> response = chatMessageService.getChatHistory(recipientId, principal, page - 1, size);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/mark-as-read/{messageId}")
    @Operation(
            summary = "Xabarni o'qilgan deb belgilash",
            description = "Berilgan messageId bo'yicha xabar holatini o'qilgan deb belgilaydi."
    )
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