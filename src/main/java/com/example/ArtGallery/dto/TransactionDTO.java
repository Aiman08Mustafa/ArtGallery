package com.example.ArtGallery.dto;

import com.example.ArtGallery.entity.enums.PaymentMethod;
import com.example.ArtGallery.entity.enums.TransactionStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long id;
    private LocalDateTime transactionDate;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private TransactionStatus status;
    private Long artworkId;
    private String artworkTitle;
    private Long userId;
    private String userEmail;
}