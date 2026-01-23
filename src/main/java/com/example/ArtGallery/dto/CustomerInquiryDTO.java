package com.example.ArtGallery.dto;

import com.example.ArtGallery.entity.enums.InquiryStatus;
import com.example.ArtGallery.entity.enums.InquiryType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerInquiryDTO {
    private Long id;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private InquiryType inquiryType;
    private String message;
    private InquiryStatus status;
    private LocalDateTime inquiryDate;
    private LocalDateTime responseDate;
    private String responseMessage;
    private Long assignedToId;
    private String assignedToName;
}