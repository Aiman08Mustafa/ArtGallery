package com.example.ArtGallery.repository;

import com.example.ArtGallery.entity.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    List<Exhibition> findByStartDateAfter(LocalDate date);
    List<Exhibition> findByEndDateBefore(LocalDate date);
    List<Exhibition> findByLocationContaining(String location);
}