package com.example.ArtGallery.controller;

import com.example.ArtGallery.dto.ArtworkDTO;
import com.example.ArtGallery.entity.enums.ArtworkStatus;
import com.example.ArtGallery.service.ArtworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/artworks")
@RequiredArgsConstructor
public class ArtworkController {

    private final ArtworkService artworkService;

    @GetMapping
    public ResponseEntity<List<ArtworkDTO>> getAllArtworks() {
        return ResponseEntity.ok(artworkService.getAllArtworks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtworkDTO> getArtworkById(@PathVariable Long id) {
        return ResponseEntity.ok(artworkService.getArtworkById(id));
    }

    @PostMapping
    public ResponseEntity<ArtworkDTO> createArtwork(@Valid @RequestBody ArtworkDTO artworkDTO) {
        ArtworkDTO createdArtwork = artworkService.createArtwork(artworkDTO);
        return new ResponseEntity<>(createdArtwork, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtworkDTO> updateArtwork(
            @PathVariable Long id,
            @Valid @RequestBody ArtworkDTO artworkDTO) {
        return ResponseEntity.ok(artworkService.updateArtwork(id, artworkDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtwork(@PathVariable Long id) {
        artworkService.deleteArtwork(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ArtworkDTO>> getArtworksByStatus(@PathVariable ArtworkStatus status) {
        return ResponseEntity.ok(artworkService.getArtworksByStatus(status));
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<ArtworkDTO>> getArtworksByArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(artworkService.getArtworksByArtist(artistId));
    }

    @GetMapping("/public/available")
    public ResponseEntity<List<ArtworkDTO>> getAvailableArtworks() {
        return ResponseEntity.ok(artworkService.getArtworksByStatus(ArtworkStatus.AVAILABLE));
    }
}
