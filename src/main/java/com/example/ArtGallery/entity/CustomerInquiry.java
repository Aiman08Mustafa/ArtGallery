package com.example.ArtGallery.entity;

import com.example.ArtGallery.entity.enums.InquiryStatus;
import com.example.ArtGallery.entity.enums.InquiryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_inquiries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String customerPhone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryType inquiryType;

    @Column(nullable = false, length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryStatus status;

    @Column(nullable = false)
    private LocalDateTime inquiryDate;

    private LocalDateTime responseDate;

    @Column(length = 2000)
    private String responseMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    @PrePersist
    protected void onCreate() {
        inquiryDate = LocalDateTime.now();
        if (status == null) {
            status = InquiryStatus.NEW;
        }
    }
}