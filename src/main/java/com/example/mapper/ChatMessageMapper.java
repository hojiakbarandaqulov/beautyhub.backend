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
     ChatMessageDTO toDto(ChatMessageEntity entity);

}
