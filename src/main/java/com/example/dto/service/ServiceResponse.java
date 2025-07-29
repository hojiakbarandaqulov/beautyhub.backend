package com.example.dto.service;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceResponse {

    @NotNull
    private Long id;
    @NotBlank(message = "name required")
    private String name;
    @NotNull(message = "price required")
    private Double price;
    @NotNull(message = "duration required")
    private Integer duration;
    @NotNull(message = "salonId required")
    private Long salonId;
    @NotNull(message = "categoryId required")
    private Long categoryId;
    @NotBlank(message = "categoryName required")
    private String categoryName;
}