package com.example.dto.master;

import com.example.entity.AttachEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class MasterCreateDto {
    private String name;
    private String specialization;
    private Double rating;
    @NotNull(message = "salonId required")
    private Long salonId;
    @NotNull(message = "photoId required")
    private AttachEntity photoId;
    @NotNull(message = "serviceId required")
    private Set<Long> serviceId;
}
