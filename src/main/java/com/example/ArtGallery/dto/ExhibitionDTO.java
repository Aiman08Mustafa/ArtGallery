package com.example.ArtGallery.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExhibitionDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private List<ArtworkDTO> artworks;
}