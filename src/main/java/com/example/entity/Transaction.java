package com.example.entity;


import com.example.enums.TransactionStatus;
import com.example.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id",insertable = false, updatable = false)
    private CardEntity card;
    @Column(name = "card_id")
    private Long cardId;
}

