package org.example.ges_concours.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.ges_concours.Entity.Utilisateur;

@Data
@Builder
@AllArgsConstructor
public class UtilisateurResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String role;

    public static UtilisateurResponse fromEntity(Utilisateur u) {
        return UtilisateurResponse.builder()
                .id(u.getId())
                .nom(u.getNom())
                .prenom(u.getPrenom())
                .email(u.getEmail())
                .telephone(u.getTelephone())
                .role(u.getRole().name())
                .build();
    }
}