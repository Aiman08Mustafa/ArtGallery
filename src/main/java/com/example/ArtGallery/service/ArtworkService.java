package com.example.ArtGallery.service;

import com.example.ArtGallery.dto.ArtworkDTO;
import com.example.ArtGallery.entity.Artwork;
import com.example.ArtGallery.entity.User;
import com.example.ArtGallery.entity.enums.ArtworkStatus;
import com.example.ArtGallery.exception.ResourceNotFoundException;
import com.example.ArtGallery.repository.ArtworkRepository;
import com.example.ArtGallery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'ARTIST', 'CUSTOMER')")
    public List<ArtworkDTO> getAllArtworks() {
        return artworkRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'ARTIST')")
    public ArtworkDTO getArtworkById(Long id) {
        Artwork artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with id: " + id));
        return convertToDTO(artwork);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'ARTIST')")
    @Transactional
    public ArtworkDTO createArtwork(ArtworkDTO artworkDTO) {
        Artwork artwork = new Artwork();
        updateArtworkFromDTO(artwork, artworkDTO);

        if (artworkDTO.getArtistId() != null) {
            User artist = userRepository.findById(artworkDTO.getArtistId())
                    .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
            artwork.setArtist(artist);
        }

        Artwork savedArtwork = artworkRepository.save(artwork);
        return convertToDTO(savedArtwork);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'ARTIST')")
    @Transactional
    public ArtworkDTO updateArtwork(Long id, ArtworkDTO artworkDTO) {
        Artwork artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with id: " + id));

        updateArtworkFromDTO(artwork, artworkDTO);

        if (artworkDTO.getArtistId() != null &&
                (artwork.getArtist() == null || !artwork.getArtist().getId().equals(artworkDTO.getArtistId()))) {
            User artist = userRepository.findById(artworkDTO.getArtistId())
                    .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
            artwork.setArtist(artist);
        }

        Artwork updatedArtwork = artworkRepository.save(artwork);
        return convertToDTO(updatedArtwork);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Transactional
    public void deleteArtwork(Long id) {
        if (!artworkRepository.existsById(id)) {
            throw new ResourceNotFoundException("Artwork not found with id: " + id);
        }
        artworkRepository.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'ARTIST', 'CUSTOMER')")
    public List<ArtworkDTO> getArtworksByStatus(ArtworkStatus status) {
        return artworkRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'ARTIST')")
    public List<ArtworkDTO> getArtworksByArtist(Long artistId) {
        return artworkRepository.findByArtistId(artistId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ArtworkDTO convertToDTO(Artwork artwork) {
        ArtworkDTO dto = new ArtworkDTO();
        dto.setId(artwork.getId());
        dto.setTitle(artwork.getTitle());
        dto.setDescription(artwork.getDescription());
        dto.setType(artwork.getType());
        dto.setMedium(artwork.getMedium());
        dto.setPrice(artwork.getPrice());
        dto.setWidth(artwork.getWidth());
        dto.setHeight(artwork.getHeight());
        dto.setDepth(artwork.getDepth());
        dto.setCreationDate(artwork.getCreationDate());
        dto.setStatus(artwork.getStatus());
        dto.setAddedDate(artwork.getAddedDate());

        if (artwork.getArtist() != null) {
            dto.setArtistId(artwork.getArtist().getId());
            dto.setArtistName(artwork.getArtist().getFirstName() + " " + artwork.getArtist().getLastName());
        }

        if (artwork.getExhibition() != null) {
            dto.setExhibitionId(artwork.getExhibition().getId());
            dto.setExhibitionTitle(artwork.getExhibition().getTitle());
        }

        return dto;
    }

    private void updateArtworkFromDTO(Artwork artwork, ArtworkDTO dto) {
        artwork.setTitle(dto.getTitle());
        artwork.setDescription(dto.getDescription());
        artwork.setType(dto.getType());
        artwork.setMedium(dto.getMedium());
        artwork.setPrice(dto.getPrice());
        artwork.setWidth(dto.getWidth());
        artwork.setHeight(dto.getHeight());
        artwork.setDepth(dto.getDepth());
        artwork.setCreationDate(dto.getCreationDate());
        if (dto.getStatus() != null) {
            artwork.setStatus(dto.getStatus());
        }
    }
}