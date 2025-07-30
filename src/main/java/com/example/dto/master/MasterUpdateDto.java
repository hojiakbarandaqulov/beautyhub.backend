package com.example.dto.master;

import com.example.entity.AttachEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class MasterUpdateDto {
    private String name;
    private String specialization;
    private Double rating;
    @NotNull(message = "photoId required")
    private AttachEntity photoId;
    @NotNull(message = "serviceId required")
    private Set<Long> serviceId;
}
