package org.example.ges_concours.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ges_concours.Entity.Candidature;
import org.example.ges_concours.Entity.PieceJustificative;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidatureResponse {

    private Long id;
    private LocalDateTime dateDepot;
    private Candidature.Statut statut;

    private Long concoursId;
    private String concoursTitre;

    private Long candidatId;
    private String candidatNom;
    private String candidatPrenom;
    private String candidatEmail;
    private String candidatTelephone;

    private List<PieceDto> pieces;

    public static CandidatureResponse fromEntity(Candidature c) {
        List<PieceDto> pieces = c.getPieces() == null
                ? List.of()
                : c.getPieces().stream()
                .map(p -> new PieceDto(p.getId(), p.getType().name(), p.getCheminFichier(), p.getNomOriginal()))
                .collect(Collectors.toList());

        return CandidatureResponse.builder()
                .id(c.getId())
                .dateDepot(c.getDateDepot())
                .statut(c.getStatut())
                .concoursId(c.getConcours().getId())
                .concoursTitre(c.getConcours().getNom())
                .candidatId(c.getCandidat().getId())
                .candidatNom(c.getCandidat().getNom())
                .candidatPrenom(c.getCandidat().getPrenom())
                .candidatEmail(c.getCandidat().getEmail())
                .candidatTelephone(c.getCandidat().getTelephone())
                .pieces(pieces)
                .build();
    }

    @Data
    @AllArgsConstructor
    public static class PieceDto {
        private Long id;
        private String type;
        private String url;
        private String nomOriginal;
    }
}