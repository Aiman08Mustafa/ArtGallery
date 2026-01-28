package com.example.ArtGallery.service;

import com.example.ArtGallery.dto.CustomerInquiryDTO;
import com.example.ArtGallery.entity.CustomerInquiry;
import com.example.ArtGallery.entity.enums.InquiryStatus;
import com.example.ArtGallery.entity.enums.InquiryType;
import com.example.ArtGallery.exception.ResourceNotFoundException;
import com.example.ArtGallery.repository.CustomerInquiryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerInquiryServiceTests {
    @Mock
    private CustomerInquiryRepository customerInquiryRepository;

    @InjectMocks
    private CustomerInquiryService customerInquiryService;

    @Test
    void getCustomerInquiryById_whenExists_shouldReturnCustomerInquiryDTO(){
        CustomerInquiry inquiry = mockCustomerInquiry();

        when(customerInquiryRepository.findById(1L))
                .thenReturn(Optional.of(inquiry));

        CustomerInquiryDTO result = customerInquiryService.getInquiryById(1L);

        assertNotNull(result);
        assertEquals("Aiman", result.getCustomerName());
        verify(customerInquiryRepository).findById(1L);
    }

    @Test
    void getCustomerInquiryById_whenNoExists_shouldThrowException(){
        when(customerInquiryRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> customerInquiryService.getInquiryById(1L));
    }

   @Test
   void createInquiry_whenValidDTO_shouldSaveInquiry(){
       CustomerInquiryDTO inquiryDTO = new CustomerInquiryDTO();
       inquiryDTO.setCustomerName("John");
       inquiryDTO.setCustomerEmail("john@gmail.com");
       inquiryDTO.setInquiryType(InquiryType.PRICING_INQUIRY);
       inquiryDTO.setStatus(InquiryStatus.NEW);
       inquiryDTO.setInquiryDate(LocalDateTime.now());

       when(customerInquiryRepository.save(any(CustomerInquiry.class)))
               .thenAnswer(invocation -> invocation.getArgument(0));

       CustomerInquiryDTO result = customerInquiryService.createInquiry(inquiryDTO);

       assertNotNull(result);
       assertEquals("John", result.getCustomerName());
       verify(customerInquiryRepository).save(any(CustomerInquiry.class));
   }

   @Test
   void deleteCustomerInquiry_whenExists_shouldDeleteCustomerInquiry(){
        when(customerInquiryRepository.existsById(1L))
                .thenReturn(true);

        customerInquiryService.deleteInquiry(1L);

        verify(customerInquiryRepository).deleteById(1L);

   }

    private CustomerInquiry mockCustomerInquiry(){
        CustomerInquiry inquiry = new CustomerInquiry();
        inquiry.setId(1L);
        inquiry.setCustomerName("Aiman");
        inquiry.setCustomerEmail("aiman@gmail.com");
        inquiry.setInquiryType(InquiryType.ARTWORK_INQUIRY);
        inquiry.setStatus(InquiryStatus.IN_PROGRESS);
        inquiry.setInquiryDate(LocalDateTime.now());
        return inquiry;
    }

}
