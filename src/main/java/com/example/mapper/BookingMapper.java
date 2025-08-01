package com.example.mapper;


import com.example.dto.booking.BookingDto;
import com.example.dto.booking.BookingResponse;
import com.example.entity.home_pages.BookingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDto toDto(BookingEntity booking);
}