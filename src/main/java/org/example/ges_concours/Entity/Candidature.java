package org.example.ges_concours.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "candidatures")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateDepot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Statut statut;

    private String cvPath;

    private String photoPath;

    private String diplomePath;


    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    private Utilisateur candidat;


    @ManyToOne
    @JoinColumn(name = "concours_id", nullable = false)
    private Concours concours;
    @OneToMany(
            mappedBy = "candidature",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PieceJustificative> pieces;

    public enum Statut {
        EN_ATTENTE,
        EN_ATTENTE_DELIBERATION,
        ADMIS,
        REFUSE,
        LISTE_ATTENTE
    }
}