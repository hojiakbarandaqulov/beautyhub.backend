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

    private String transactionId; // Click tomonidan yuborilgan transaction
    private Long amount;
    private String phone;// foydalanuvchi raqami
    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // NEW, SUCCESS, FAILED

    private String clickTransId;
    private String clickPaydocId;

    private LocalDateTime createdAt;

    private LocalDateTime createTime=LocalDateTime.now();
    private LocalDateTime updateTime;
}

