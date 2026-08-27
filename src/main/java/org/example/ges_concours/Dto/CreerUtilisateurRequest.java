package org.example.ges_concours.Dto;

import lombok.Data;
import org.example.ges_concours.Entity.Utilisateur;

@Data
public class CreerUtilisateurRequest {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String motDePasse;
    private Utilisateur.Role role;
}