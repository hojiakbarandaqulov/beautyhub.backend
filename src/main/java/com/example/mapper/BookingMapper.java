package com.example.mapper;


import com.example.dto.booking.BookingDto;
import com.example.dto.booking.BookingResponse;
import com.example.entity.home_pages.BookingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "salonName", source = "salon.name")
    @Mapping(target = "serviceName", source = "service.name")
    @Mapping(target = "masterName", source = "master.name")
    BookingResponse toResponse(BookingEntity booking);

    @Mapping(target = "salonName", source = "salon.name")
    @Mapping(target = "serviceName", source = "service.name")
    @Mapping(target = "masterName", source = "master.name")
    BookingDto toDto(BookingEntity booking);
}