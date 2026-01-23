package com.example.ArtGallery.repository;

import com.example.ArtGallery.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByArtworkId(Long artworkId);
    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
}