package com.example.ArtGallery.repository;

import com.example.ArtGallery.entity.Artwork;
import com.example.ArtGallery.entity.enums.ArtworkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    List<Artwork> findByArtistId(Long artistId);
    List<Artwork> findByStatus(ArtworkStatus status);
    List<Artwork> findByExhibitionId(Long exhibitionId);
    List<Artwork> findByPriceBetween(Double minPrice, Double maxPrice);
    List<Artwork> findByType(String type);
}
