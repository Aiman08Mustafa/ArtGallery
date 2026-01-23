package com.example.ArtGallery.controller;

import com.example.ArtGallery.dto.GalleryEventDTO;
import com.example.ArtGallery.service.GalleryEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class GalleryEventController {

    private final GalleryEventService eventService;

    @GetMapping
    public ResponseEntity<List<GalleryEventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GalleryEventDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PostMapping
    public ResponseEntity<GalleryEventDTO> createEvent(@Valid @RequestBody GalleryEventDTO eventDTO) {
        GalleryEventDTO createdEvent = eventService.createEvent(eventDTO);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GalleryEventDTO> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody GalleryEventDTO eventDTO) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<GalleryEventDTO> registerForEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.registerForEvent(id));
    }
}
