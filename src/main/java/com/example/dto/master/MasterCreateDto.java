package com.example.dto.master;

import com.example.entity.AttachEntity;
import com.example.entity.home_pages.SalonEntity;
import com.example.entity.home_pages.ServiceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MasterCreateDto {
    private String name;
    private String specialization;
    @NotNull(message = "salonId required")
    private Long salonId;
    @NotNull(message = "photoId required")
    private String photoId;
    @NotNull(message = "serviceId required")
    private Long serviceId;
}
