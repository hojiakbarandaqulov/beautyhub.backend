package com.example.mapper;

import com.example.dto.chat.ChatMessageDTO;
import com.example.entity.ChatMessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

     @Mapping(source = "sender.id", target = "senderId")
     @Mapping(source = "recipient.id", target = "recipientId")
     ChatMessageDTO toDto(ChatMessageEntity savedMessage);


     ChatMessageDTO toChatHistoryMapper(ChatMessageEntity entity);

//     List<ChatMessageDTO> toDtoList(List<ChatMessageEntity> entityList);

     default String senderFullName(ChatMessageEntity entity) {
          if (entity.getSender() == null) return null;
          return entity.getSender().getFullName();
     }

     default String recipientFullName(ChatMessageEntity entity) {
          if (entity.getRecipient() == null) return null;
          return entity.getRecipient().getFullName();
     }
}
