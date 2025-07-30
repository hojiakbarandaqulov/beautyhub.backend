package com.example.dto.master;

import com.example.entity.AttachEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MasterUpdateDto {
    private String name;
    private String specialization;
    @NotNull(message = "photoId required")
    private String photoId;
    @NotNull(message = "serviceId required")
    private Long serviceId;
}
