package com.example.dto.transaction;

import com.example.enums.TransactionStatus;
import com.example.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long id;
    private Double amount;
    private LocalDateTime createdAt;
    private TransactionType type;
    private TransactionStatus status;
    private String description;
    private Long cardId;
}
