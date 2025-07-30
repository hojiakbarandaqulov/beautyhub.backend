package com.example.mapper;

import com.example.dto.root.RootCreateDTO;
import com.example.dto.root.RootDTO;
import com.example.entity.Root;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RootMapper {
    RootMapper INSTANCE = Mappers.getMapper(RootMapper.class);

    RootCreateDTO toRootCreateDto(Root root);
    Root toCreateRootEntity(RootCreateDTO rootDTO);

    RootDTO toDto(Root root);
}