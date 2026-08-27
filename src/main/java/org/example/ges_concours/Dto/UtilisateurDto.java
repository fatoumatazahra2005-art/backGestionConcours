package org.example.ges_concours.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDto {
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private String telephone;
    private String role;
}