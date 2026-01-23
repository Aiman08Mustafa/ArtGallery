package com.example.ArtGallery.controller;


import com.example.ArtGallery.dto.ExhibitionDTO;
import com.example.ArtGallery.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/exhibitions")
@RequiredArgsConstructor
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    @GetMapping
    public ResponseEntity<List<ExhibitionDTO>> getAllExhibitions() {
        return ResponseEntity.ok(exhibitionService.getAllExhibitions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExhibitionDTO> getExhibitionById(@PathVariable Long id) {
        return ResponseEntity.ok(exhibitionService.getExhibitionById(id));
    }

    @PostMapping
    public ResponseEntity<ExhibitionDTO> createExhibition(@Valid @RequestBody ExhibitionDTO exhibitionDTO) {
        ExhibitionDTO createdExhibition = exhibitionService.createExhibition(exhibitionDTO);
        return new ResponseEntity<>(createdExhibition, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExhibitionDTO> updateExhibition(
            @PathVariable Long id,
            @Valid @RequestBody ExhibitionDTO exhibitionDTO) {
        return ResponseEntity.ok(exhibitionService.updateExhibition(id, exhibitionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExhibition(@PathVariable Long id) {
        exhibitionService.deleteExhibition(id);
        return ResponseEntity.noContent().build();
    }
}
