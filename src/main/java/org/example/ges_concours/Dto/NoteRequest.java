package org.example.ges_concours.Dto;

import lombok.Data;

@Data
public class NoteRequest {
    private Long candidatureId;
    private Long epreuveId;
    private Double valeur;
}