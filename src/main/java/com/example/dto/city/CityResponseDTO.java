package com.example.dto.city;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.catalina.LifecycleState;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CityResponseDTO {
    private Long id;
    @NotBlank
    private String nameUz;

    @NotBlank
    private String nameRu;

    @NotBlank
    private String nameEn;

    private List<DistrictDTO> districts;

}
