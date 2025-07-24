package com.example.mapper;

import com.example.dto.chat.ChatMessageDTO;
import com.example.entity.ChatMessageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

     ChatMessageDTO toDto(ChatMessageEntity savedMessage);

}
