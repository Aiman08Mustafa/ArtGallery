package com.example.ArtGallery.dto;

import com.example.ArtGallery.entity.enums.ArtworkStatus;
import com.example.ArtGallery.entity.enums.ArtworkType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ArtworkDTO {
    private Long id;
    private String title;
    private String description;
    private ArtworkType type;
    private String medium;
    private BigDecimal price;
    private Integer width;
    private Integer height;
    private Integer depth;
    private LocalDate creationDate;
    private ArtworkStatus status;
    private LocalDate addedDate;
    private Long artistId;
    private String artistName;
    private Long exhibitionId;
    private String exhibitionTitle;
}