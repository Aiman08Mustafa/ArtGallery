package com.example.ArtGallery.repository;

import com.example.ArtGallery.entity.GalleryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GalleryEventRepository extends JpaRepository<GalleryEvent, Long> {
    List<GalleryEvent> findByStartDateTimeAfter(LocalDateTime date);
    List<GalleryEvent> findByEventType(String eventType);
}
