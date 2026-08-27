package org.example.ges_concours.Service;

import org.example.ges_concours.Dto.ResultatResponse;
import org.example.ges_concours.Entity.Candidature;
import org.example.ges_concours.Entity.Concours;
import org.example.ges_concours.Repository.ConcoursRepository;
import org.example.ges_concours.Repository.ResultatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultatService {

    private final ResultatRepository resultatRepository;
    private final ConcoursRepository concoursRepository;

    public ResultatService(
            ResultatRepository resultatRepository,
            ConcoursRepository concoursRepository) {

        this.resultatRepository = resultatRepository;
        this.concoursRepository = concoursRepository;
    }


    public List<ResultatResponse> getAdmis() {

        return resultatRepository
                .findByStatut(Candidature.Statut.ADMIS)
                .stream()
                .map(this::convertir)
                .toList();
    }

    // Tous les candidats sur liste d'attente
    public List<ResultatResponse> getListeAttente() {

        return resultatRepository
                .findByStatut(Candidature.Statut.LISTE_ATTENTE)
                .stream()
                .map(this::convertir)
                .toList();
    }


    public List<Candidature> getResultatsByConcours(Long concoursId) {

        Concours concours = concoursRepository.findById(concoursId)
                .orElseThrow(() ->
                        new RuntimeException("Concours introuvable"));

        return resultatRepository.findByConcoursAndStatutIn(
                concours,
                List.of(
                        Candidature.Statut.ADMIS,
                        Candidature.Statut.LISTE_ATTENTE
                )
        );
    }


    public List<ResultatResponse> getResultats() {

        return resultatRepository.findByStatutIn(
                        List.of(
                                Candidature.Statut.ADMIS,
                                Candidature.Statut.LISTE_ATTENTE,
                                Candidature.Statut.REFUSE
                        )
                )
                .stream()
                .map(this::convertir)
                .toList();
    }

    public List<ResultatResponse> getRefuses() {

        return resultatRepository
                .findByStatut(Candidature.Statut.REFUSE)
                .stream()
                .map(this::convertir)
                .toList();
    }

    private ResultatResponse convertir(Candidature candidature) {

        return ResultatResponse.builder()
                .candidatureId(candidature.getId())
                .nomCandidat(candidature.getCandidat().getNom())
                .prenomCandidat(candidature.getCandidat().getPrenom())
                .concours(candidature.getConcours().getNom())
                .dateDepot(candidature.getDateDepot())
                .statut(candidature.getStatut().name())
                .build();
    }
}