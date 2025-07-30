package com.example.dto.master;

import com.example.entity.AttachEntity;
import lombok.Data;

import java.util.Set;

@Data
public class MasterResponseDto {
    private Long id;
    private String name;
    private String specialization;
    private Double rating;
    private String photoId;
    private Long salonId;
    private Set<Long> serviceId;
}
