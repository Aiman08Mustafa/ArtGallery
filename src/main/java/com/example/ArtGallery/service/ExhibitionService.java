package com.example.ArtGallery.service;


import com.example.ArtGallery.dto.ExhibitionDTO;
import com.example.ArtGallery.entity.Exhibition;
import com.example.ArtGallery.exception.ResourceNotFoundException;
import com.example.ArtGallery.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'ARTIST', 'CUSTOMER')")
    public List<ExhibitionDTO> getAllExhibitions() {
        return exhibitionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'ARTIST', 'CUSTOMER')")
    public ExhibitionDTO getExhibitionById(Long id) {
        Exhibition exhibition = exhibitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exhibition not found with id: " + id));
        return convertToDTO(exhibition);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Transactional
    public ExhibitionDTO createExhibition(ExhibitionDTO exhibitionDTO) {
        Exhibition exhibition = new Exhibition();
        updateExhibitionFromDTO(exhibition, exhibitionDTO);

        Exhibition savedExhibition = exhibitionRepository.save(exhibition);
        return convertToDTO(savedExhibition);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Transactional
    public ExhibitionDTO updateExhibition(Long id, ExhibitionDTO exhibitionDTO) {
        Exhibition exhibition = exhibitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exhibition not found with id: " + id));

        updateExhibitionFromDTO(exhibition, exhibitionDTO);

        Exhibition updatedExhibition = exhibitionRepository.save(exhibition);
        return convertToDTO(updatedExhibition);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Transactional
    public void deleteExhibition(Long id) {
        if (!exhibitionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Exhibition not found with id: " + id);
        }
        exhibitionRepository.deleteById(id);
    }

    private ExhibitionDTO convertToDTO(Exhibition exhibition) {
        ExhibitionDTO dto = new ExhibitionDTO();
        dto.setId(exhibition.getId());
        dto.setTitle(exhibition.getTitle());
        dto.setDescription(exhibition.getDescription());
        dto.setStartDate(exhibition.getStartDate());
        dto.setEndDate(exhibition.getEndDate());
        dto.setLocation(exhibition.getLocation());
        return dto;
    }

    private void updateExhibitionFromDTO(Exhibition exhibition, ExhibitionDTO dto) {
        exhibition.setTitle(dto.getTitle());
        exhibition.setDescription(dto.getDescription());
        exhibition.setStartDate(dto.getStartDate());
        exhibition.setEndDate(dto.getEndDate());
        exhibition.setLocation(dto.getLocation());
    }
}