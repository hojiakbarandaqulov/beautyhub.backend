package com.example.dto.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceCreateRequest {
    @NotNull
    private long salonId;

    @NotBlank(message = "Xizmat nomi bo'sh bo'lishi mumkin emas")
    @Size(min = 3, max = 100)
    private String name;

    @Size(max = 500, message = "Tavsif 500 belgidan oshmasligi kerak")
    private String description;

    @Min(value = 5, message = "Davomiyligi kamida 5 daqiqa bo'lishi kerak")
    @Max(value = 300, message = "Davomiyligi ko'pi bilan 300 daqiqa bo'lishi kerak")
    private Integer durationMinutes;

    @DecimalMin(value = "0.0", message = "Narx manfiy bo'lishi mumkin emas")
    private Double price;
}
