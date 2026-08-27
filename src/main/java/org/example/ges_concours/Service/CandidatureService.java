package org.example.ges_concours.Service;

import org.example.ges_concours.Entity.Candidature;
import org.example.ges_concours.Entity.Concours;
import org.example.ges_concours.Entity.Utilisateur;
import org.example.ges_concours.Repository.CandidatureRepository;
import org.example.ges_concours.Repository.ConcoursRepository;
import org.example.ges_concours.Repository.UtilisateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CandidatureService {

    private final CandidatureRepository candidatureRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ConcoursRepository concoursRepository;

    public CandidatureService(
            CandidatureRepository candidatureRepository,
            UtilisateurRepository utilisateurRepository,
            ConcoursRepository concoursRepository) {

        this.candidatureRepository = candidatureRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.concoursRepository = concoursRepository;
    }

    public Candidature creer(Long concoursId, String email) {

        Utilisateur candidat = utilisateurRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        Concours concours = concoursRepository.findById(concoursId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Concours introuvable"));

        if (candidatureRepository
                .findByCandidatAndConcours(candidat, concours)
                .isPresent()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Vous avez déjà candidaté à ce concours"
            );
        }

        Candidature candidature = Candidature.builder()
                .candidat(candidat)
                .concours(concours)
                .dateDepot(LocalDateTime.now())
                .statut(Candidature.Statut.EN_ATTENTE)
                .build();

        return candidatureRepository.save(candidature);
    }

    public Candidature changerStatut(
            Long id,
            Candidature.Statut nouveauStatut) {

        Candidature candidature = candidatureRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidature introuvable"));

        candidature.setStatut(nouveauStatut);

        return candidatureRepository.save(candidature);
    }

    public List<Candidature> getAll() {
        return candidatureRepository.findAllWithPieces();
    }

    public Optional<Candidature> getById(Long id) {
        return candidatureRepository.findByIdWithPieces(id);
    }

    public List<Candidature> getByCandidat(Long candidatId) {
        Utilisateur candidat = utilisateurRepository.findById(candidatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidat introuvable"));
        return candidatureRepository.findByCandidatWithPieces(candidat);
    }

    public List<Candidature> getByConcours(Long concoursId) {
        Concours concours = concoursRepository.findById(concoursId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Concours introuvable"));
        return candidatureRepository.findByConcoursWithPieces(concours);
    }

    public List<Candidature> getByEmail(String email) {
        Utilisateur candidat = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
        return candidatureRepository.findByCandidatWithPieces(candidat);
    }

    public void delete(Long id) {
        if (!candidatureRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidature introuvable");
        }
        candidatureRepository.deleteById(id);
    }

    public void annulerParCandidat(Long candidatureId, String emailConnecte) {

        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidature introuvable"));

        if (!candidature.getCandidat().getEmail().equals(emailConnecte)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cette candidature ne vous appartient pas");
        }

        if (candidature.getStatut() == Candidature.Statut.ADMIS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Impossible d'annuler une candidature déjà admise");
        }

        candidatureRepository.delete(candidature);
    }
}