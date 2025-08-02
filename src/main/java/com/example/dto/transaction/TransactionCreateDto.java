package com.example.dto.transaction;

import com.example.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionCreateDto {

    @NotNull
    private Long cardId;

    @NotNull
    private Double amount;

    private String description;

    @NotNull
    private TransactionType type;
}
