package com.example.ArtGallery.service;

import com.example.ArtGallery.dto.ArtworkDTO;
import com.example.ArtGallery.entity.Artwork;
import com.example.ArtGallery.entity.enums.ArtworkStatus;
import com.example.ArtGallery.entity.enums.ArtworkType;
import com.example.ArtGallery.exception.ResourceNotFoundException;
import com.example.ArtGallery.repository.ArtworkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArtworkServiceTest {
    @Mock
    private ArtworkRepository artworkRepository;

    @InjectMocks
    private ArtworkService artworkService;

    @Test
    void getArtworkById_whenExists_shouldReturnArtworkDTO(){
        Artwork artwork = mockArtwork();

        when(artworkRepository.findById(1L))
                .thenReturn(Optional.of(artwork));

        ArtworkDTO result = artworkService.getArtworkById(1L);

        assertNotNull(result);
        assertEquals("Mona Lisa", result.getTitle());
        verify(artworkRepository).findById(1L);

    }

    @Test
    void getArtworkById_whenNoExists_shouldThrowException(){
        when(artworkRepository.findById(4L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> artworkService.getArtworkById(4L));

    }

    @Test
    void createArtwork_whenValidDTO_shouldSaveArtwork(){
        ArtworkDTO artworkDTO = new ArtworkDTO();
        artworkDTO.setTitle("Starry Night");
        artworkDTO.setType(ArtworkType.PAINTING);
        artworkDTO.setMedium("Oil");
        artworkDTO.setPrice(BigDecimal.valueOf(2000));
        artworkDTO.setCreationDate(LocalDate.now());

        when(artworkRepository.save(any(Artwork.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArtworkDTO result = artworkService.createArtwork(artworkDTO);

        assertNotNull(result);
        assertEquals("Starry Night", result.getTitle());
        verify(artworkRepository).save(any(Artwork.class));
    }

    @Test
    void deleteArtwork_whenExists_shouldDelete(){
        when(artworkRepository.existsById(1L))
                .thenReturn(true);

        artworkService.deleteArtwork(1L);

        verify(artworkRepository).deleteById(1L);
    }

    @Test
    void deleteArtwork_whenNotExists_shoildThrowException(){
        when(artworkRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> artworkService.deleteArtwork(1L));
    }


    private Artwork mockArtwork(){
        Artwork artwork = new Artwork();
        artwork.setId(1L);
        artwork.setTitle("Mona Lisa");
        artwork.setType(ArtworkType.PAINTING);
        artwork.setMedium("Oil");
        artwork.setPrice(BigDecimal.valueOf(1000));
        artwork.setCreationDate(LocalDate.now());
        artwork.setStatus(ArtworkStatus.AVAILABLE);
        return artwork;
    }
}
