package com.example.ArtGallery.service;

import com.example.ArtGallery.dto.CustomerInquiryDTO;
import com.example.ArtGallery.entity.CustomerInquiry;
import com.example.ArtGallery.entity.User;
import com.example.ArtGallery.entity.enums.InquiryStatus;
import com.example.ArtGallery.exception.ResourceNotFoundException;
import com.example.ArtGallery.repository.CustomerInquiryRepository;
import com.example.ArtGallery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerInquiryService {

    private final CustomerInquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public List<CustomerInquiryDTO> getAllInquiries() {
        return inquiryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public CustomerInquiryDTO getInquiryById(Long id) {
        CustomerInquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry not found with id: " + id));
        return convertToDTO(inquiry);
    }

    @PreAuthorize("permitAll()")
    @Transactional
    public CustomerInquiryDTO createInquiry(CustomerInquiryDTO inquiryDTO) {
        CustomerInquiry inquiry = new CustomerInquiry();
        updateInquiryFromDTO(inquiry, inquiryDTO);

        CustomerInquiry savedInquiry = inquiryRepository.save(inquiry);
        return convertToDTO(savedInquiry);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Transactional
    public CustomerInquiryDTO updateInquiry(Long id, CustomerInquiryDTO inquiryDTO) {
        CustomerInquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry not found with id: " + id));

        updateInquiryFromDTO(inquiry, inquiryDTO);

        if (inquiryDTO.getAssignedToId() != null &&
                (inquiry.getAssignedTo() == null || !inquiry.getAssignedTo().getId().equals(inquiryDTO.getAssignedToId()))) {
            User assignedTo = userRepository.findById(inquiryDTO.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            inquiry.setAssignedTo(assignedTo);
        }

        CustomerInquiry updatedInquiry = inquiryRepository.save(inquiry);
        return convertToDTO(updatedInquiry);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Transactional
    public CustomerInquiryDTO respondToInquiry(Long id, String responseMessage) {
        CustomerInquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry not found with id: " + id));

        inquiry.setResponseMessage(responseMessage);
        inquiry.setResponseDate(LocalDateTime.now());
        inquiry.setStatus(InquiryStatus.RESOLVED);

        CustomerInquiry updatedInquiry = inquiryRepository.save(inquiry);
        return convertToDTO(updatedInquiry);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Transactional
    public void deleteInquiry(Long id) {
        if (!inquiryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inquiry not found with id: " + id);
        }
        inquiryRepository.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public List<CustomerInquiryDTO> getInquiriesByStatus(InquiryStatus status) {
        return inquiryRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CustomerInquiryDTO convertToDTO(CustomerInquiry inquiry) {
        CustomerInquiryDTO dto = new CustomerInquiryDTO();
        dto.setId(inquiry.getId());
        dto.setCustomerName(inquiry.getCustomerName());
        dto.setCustomerEmail(inquiry.getCustomerEmail());
        dto.setCustomerPhone(inquiry.getCustomerPhone());
        dto.setInquiryType(inquiry.getInquiryType());
        dto.setMessage(inquiry.getMessage());
        dto.setStatus(inquiry.getStatus());
        dto.setInquiryDate(inquiry.getInquiryDate());
        dto.setResponseDate(inquiry.getResponseDate());
        dto.setResponseMessage(inquiry.getResponseMessage());

        if (inquiry.getAssignedTo() != null) {
            dto.setAssignedToId(inquiry.getAssignedTo().getId());
            dto.setAssignedToName(inquiry.getAssignedTo().getFirstName() + " " + inquiry.getAssignedTo().getLastName());
        }

        return dto;
    }

    private void updateInquiryFromDTO(CustomerInquiry inquiry, CustomerInquiryDTO dto) {
        inquiry.setCustomerName(dto.getCustomerName());
        inquiry.setCustomerEmail(dto.getCustomerEmail());
        inquiry.setCustomerPhone(dto.getCustomerPhone());
        inquiry.setInquiryType(dto.getInquiryType());
        inquiry.setMessage(dto.getMessage());
        if (dto.getStatus() != null) {
            inquiry.setStatus(dto.getStatus());
        }
        if (dto.getResponseMessage() != null) {
            inquiry.setResponseMessage(dto.getResponseMessage());
        }
    }
}