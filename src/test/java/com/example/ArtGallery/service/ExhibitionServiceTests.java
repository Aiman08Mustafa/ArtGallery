package com.example.ArtGallery.service;

import com.example.ArtGallery.dto.ExhibitionDTO;
import com.example.ArtGallery.entity.Exhibition;
import com.example.ArtGallery.exception.ResourceNotFoundException;
import com.example.ArtGallery.repository.ExhibitionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExhibitionServiceTests {

    @Mock
    private ExhibitionRepository exhibitionRepository;

    @InjectMocks
    private ExhibitionService exhibitionService;

    @Test
    void getExhibitionById_whenExists_shouldReturnExhibitionDTO(){
        Exhibition exhibition = mockExhibition();

        when(exhibitionRepository.findById(3L))
                .thenReturn(Optional.of(exhibition));

        ExhibitionDTO result = exhibitionService.getExhibitionById(3L);

        assertNotNull(result);
        assertEquals("Art Exhibition", result.getTitle());
        verify(exhibitionRepository).findById(3L);
    }

    @Test
    void getExhibitionById_whenNotExists_shouldThrowException(){
        when(exhibitionRepository.findById(3L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> exhibitionService.getExhibitionById(3L));
    }

    @Test
    void createExhibition_whenValidDTO_shouldSaveExhibition(){
        ExhibitionDTO exhibitionDTO = new ExhibitionDTO();
        exhibitionDTO.setTitle("Modern Exhibition");
        exhibitionDTO.setLocation("Islamabad");
        exhibitionDTO.setStartDate(LocalDate.now());

        when(exhibitionRepository.save(any(Exhibition.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ExhibitionDTO result = exhibitionService.createExhibition(exhibitionDTO);

        assertNotNull(result);
        assertEquals("Modern Exhibition", result.getTitle());
        verify(exhibitionRepository).save(any(Exhibition.class));

    }

    private Exhibition mockExhibition(){
        Exhibition exhibition = new Exhibition();
        exhibition.setId(3L);
        exhibition.setTitle("Art Exhibition");
        exhibition.setLocation("Pakistan");
        exhibition.setStartDate(LocalDate.now());
        return exhibition;
    }
}
