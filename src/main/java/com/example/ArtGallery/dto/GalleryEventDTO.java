package com.example.ArtGallery.dto;

import com.example.ArtGallery.entity.enums.EventType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GalleryEventDTO {
    private Long id;
    private String title;
    private String description;
    private EventType eventType;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private Integer maxAttendees;
    private Integer currentAttendees;
}