package org.example.ges_concours.Service;

import org.example.ges_concours.Entity.Utilisateur;
import org.example.ges_concours.Repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }
    public List<Utilisateur> getAll(){
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getUserById(Long id){
        return utilisateurRepository.findById(id);
    }

    public Utilisateur save(Utilisateur utilisateur){
        return utilisateurRepository.save(utilisateur);
    }

    public void delete(Long id){
        utilisateurRepository.deleteById(id);
    }
    public Utilisateur update(Long id, Utilisateur utilisateurRequest) {

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable")
                );

        utilisateur.setNom(utilisateurRequest.getNom());
        utilisateur.setPrenom(utilisateurRequest.getPrenom());
        utilisateur.setEmail(utilisateurRequest.getEmail());
        utilisateur.setTelephone(utilisateurRequest.getTelephone());
        utilisateur.setRole(utilisateurRequest.getRole());

        return utilisateurRepository.save(utilisateur);
    }

}
