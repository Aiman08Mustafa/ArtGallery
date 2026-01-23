package com.example.ArtGallery.service;


import com.example.ArtGallery.dto.GalleryEventDTO;
import com.example.ArtGallery.entity.GalleryEvent;
import com.example.ArtGallery.exception.ResourceNotFoundException;
import com.example.ArtGallery.repository.GalleryEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GalleryEventService {

    private final GalleryEventRepository eventRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'ARTIST', 'CUSTOMER')")
    public List<GalleryEventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'ARTIST', 'CUSTOMER')")
    public GalleryEventDTO getEventById(Long id) {
        GalleryEvent event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return convertToDTO(event);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Transactional
    public GalleryEventDTO createEvent(GalleryEventDTO eventDTO) {
        GalleryEvent event = new GalleryEvent();
        updateEventFromDTO(event, eventDTO);

        GalleryEvent savedEvent = eventRepository.save(event);
        return convertToDTO(savedEvent);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Transactional
    public GalleryEventDTO updateEvent(Long id, GalleryEventDTO eventDTO) {
        GalleryEvent event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        updateEventFromDTO(event, eventDTO);

        GalleryEvent updatedEvent = eventRepository.save(event);
        return convertToDTO(updatedEvent);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @Transactional
    public GalleryEventDTO registerForEvent(Long eventId) {
        GalleryEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        if (event.getCurrentAttendees() >= event.getMaxAttendees()) {
            throw new IllegalStateException("Event is fully booked");
        }

        event.setCurrentAttendees(event.getCurrentAttendees() + 1);
        GalleryEvent updatedEvent = eventRepository.save(event);
        return convertToDTO(updatedEvent);
    }

    private GalleryEventDTO convertToDTO(GalleryEvent event) {
        GalleryEventDTO dto = new GalleryEventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setEventType(event.getEventType());
        dto.setStartDateTime(event.getStartDateTime());
        dto.setEndDateTime(event.getEndDateTime());
        dto.setLocation(event.getLocation());
        dto.setMaxAttendees(event.getMaxAttendees());
        dto.setCurrentAttendees(event.getCurrentAttendees());
        return dto;
    }

    private void updateEventFromDTO(GalleryEvent event, GalleryEventDTO dto) {
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setEventType(dto.getEventType());
        event.setStartDateTime(dto.getStartDateTime());
        event.setEndDateTime(dto.getEndDateTime());
        event.setLocation(dto.getLocation());
        event.setMaxAttendees(dto.getMaxAttendees());
        if (dto.getCurrentAttendees() != null) {
            event.setCurrentAttendees(dto.getCurrentAttendees());
        }
    }
}