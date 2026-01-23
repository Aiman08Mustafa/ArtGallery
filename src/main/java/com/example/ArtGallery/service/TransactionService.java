package com.example.ArtGallery.service;

import com.example.ArtGallery.dto.TransactionDTO;
import com.example.ArtGallery.entity.Artwork;
import com.example.ArtGallery.entity.Transaction;
import com.example.ArtGallery.entity.User;
import com.example.ArtGallery.entity.enums.ArtworkStatus;
import com.example.ArtGallery.entity.enums.TransactionStatus;
import com.example.ArtGallery.exception.ResourceNotFoundException;
import com.example.ArtGallery.repository.ArtworkRepository;
import com.example.ArtGallery.repository.TransactionRepository;
import com.example.ArtGallery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        return convertToDTO(transaction);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        updateTransactionFromDTO(transaction, transactionDTO);

        // Validate and set artwork
        Artwork artwork = artworkRepository.findById(transactionDTO.getArtworkId())
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));

        if (artwork.getStatus() != ArtworkStatus.AVAILABLE) {
            throw new IllegalStateException("Artwork is not available for purchase");
        }

        transaction.setArtwork(artwork);

        // Set user
        User user = userRepository.findById(transactionDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        transaction.setUser(user);

        // Update artwork status
        artwork.setStatus(ArtworkStatus.SOLD);
        artworkRepository.save(artwork);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDTO(savedTransaction);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Transactional
    public TransactionDTO updateTransactionStatus(Long id, TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        transaction.setStatus(status);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return convertToDTO(updatedTransaction);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public List<TransactionDTO> getTransactionsByUser(Long userId) {
        return transactionRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setAmount(transaction.getAmount());
        dto.setPaymentMethod(transaction.getPaymentMethod());
        dto.setStatus(transaction.getStatus());

        if (transaction.getArtwork() != null) {
            dto.setArtworkId(transaction.getArtwork().getId());
            dto.setArtworkTitle(transaction.getArtwork().getTitle());
        }

        if (transaction.getUser() != null) {
            dto.setUserId(transaction.getUser().getId());
            dto.setUserEmail(transaction.getUser().getEmail());
        }

        return dto;
    }

    private void updateTransactionFromDTO(Transaction transaction, TransactionDTO dto) {
        transaction.setAmount(dto.getAmount());
        transaction.setPaymentMethod(dto.getPaymentMethod());
        if (dto.getStatus() != null) {
            transaction.setStatus(dto.getStatus());
        }
    }
}
