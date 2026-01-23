package com.example.ArtGallery.controller;

import com.example.ArtGallery.dto.CustomerInquiryDTO;
import com.example.ArtGallery.entity.enums.InquiryStatus;
import com.example.ArtGallery.service.CustomerInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class CustomerInquiryController {

    private final CustomerInquiryService inquiryService;

    @GetMapping
    public ResponseEntity<List<CustomerInquiryDTO>> getAllInquiries() {
        return ResponseEntity.ok(inquiryService.getAllInquiries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerInquiryDTO> getInquiryById(@PathVariable Long id) {
        return ResponseEntity.ok(inquiryService.getInquiryById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerInquiryDTO> createInquiry(@Valid @RequestBody CustomerInquiryDTO inquiryDTO) {
        CustomerInquiryDTO createdInquiry = inquiryService.createInquiry(inquiryDTO);
        return new ResponseEntity<>(createdInquiry, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerInquiryDTO> updateInquiry(
            @PathVariable Long id,
            @Valid @RequestBody CustomerInquiryDTO inquiryDTO) {
        return ResponseEntity.ok(inquiryService.updateInquiry(id, inquiryDTO));
    }

    @PostMapping("/{id}/respond")
    public ResponseEntity<CustomerInquiryDTO> respondToInquiry(
            @PathVariable Long id,
            @RequestParam String response) {
        return ResponseEntity.ok(inquiryService.respondToInquiry(id, response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long id) {
        inquiryService.deleteInquiry(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CustomerInquiryDTO>> getInquiriesByStatus(@PathVariable InquiryStatus status) {
        return ResponseEntity.ok(inquiryService.getInquiriesByStatus(status));
    }
}
