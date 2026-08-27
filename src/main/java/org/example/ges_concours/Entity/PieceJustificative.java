package org.example.ges_concours.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pieces_justificatives")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PieceJustificative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePiece type;

    @Column(nullable = false)
    private String cheminFichier;

    @Column(nullable = false)
    private String nomOriginal;

    private LocalDateTime dateUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidature_id", nullable = false)
    @JsonIgnore
    private Candidature candidature;

    @PrePersist
    protected void onCreate() {
        this.dateUpload = LocalDateTime.now();
    }

    public enum TypePiece {
        CV,
        PHOTO,
        DIPLOME
    }
}