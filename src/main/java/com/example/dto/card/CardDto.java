package com.example.dto.card;

import lombok.Data;

@Data
public class CardDto {
    private Long id;
    private String number;
    private String holder;
    private Double balance;
}
