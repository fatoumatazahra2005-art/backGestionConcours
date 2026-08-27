package org.example.ges_concours.Dto;

import lombok.Data;

@Data
public class EpreuveRequest {
    private String nom;
    private Integer coefficient;
    private Integer duree;
    private Long concoursId;
}