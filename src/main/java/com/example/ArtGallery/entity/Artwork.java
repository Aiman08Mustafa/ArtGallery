package com.example.ArtGallery.entity;

import com.example.ArtGallery.entity.enums.ArtworkStatus;
import com.example.ArtGallery.entity.enums.ArtworkType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "artworks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArtworkType type;

    @Column(nullable = false)
    private String medium;

    @Column(nullable = false)
    private BigDecimal price;

    private Integer width;
    private Integer height;
    private Integer depth;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArtworkStatus status;

    @Column(nullable = false)
    private LocalDate addedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    @JsonBackReference
    private User artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;

    @OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @PrePersist
    protected void onCreate() {
        addedDate = LocalDate.now();
        if (status == null) {
            status = ArtworkStatus.AVAILABLE;
        }
    }
}