package org.example.ges_concours.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultatResponse {

    private Long candidatureId;

    private String nomCandidat;

    private String prenomCandidat;

    private String concours;

    private LocalDateTime dateDepot;

    private String statut;
}