package com.example.dto.master;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
