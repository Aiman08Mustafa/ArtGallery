package com.example.ArtGallery.repository;

import com.example.ArtGallery.entity.CustomerInquiry;
import com.example.ArtGallery.entity.enums.InquiryStatus;
import com.example.ArtGallery.entity.enums.InquiryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerInquiryRepository extends JpaRepository<CustomerInquiry, Long> {
    List<CustomerInquiry> findByStatus(InquiryStatus status);
    List<CustomerInquiry> findByInquiryType(InquiryType type);
    List<CustomerInquiry> findByAssignedToId(Long userId);
    List<CustomerInquiry> findByCustomerEmail(String email);
}