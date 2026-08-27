package org.example.ges_concours.Service;

import org.example.ges_concours.Dto.NoteRequest;
import org.example.ges_concours.Entity.Candidature;
import org.example.ges_concours.Entity.Epreuve;
import org.example.ges_concours.Entity.Note;
import org.example.ges_concours.Repository.CandidatureRepository;
import org.example.ges_concours.Repository.EpreuveRepository;
import org.example.ges_concours.Repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NoteService {

    private static final double SEUIL_ADMISSION = 12.0;

    private final NoteRepository noteRepository;
    private final CandidatureRepository candidatureRepository;
    private final EpreuveRepository epreuveRepository;

    public NoteService(
            NoteRepository noteRepository,
            CandidatureRepository candidatureRepository,
            EpreuveRepository epreuveRepository) {

        this.noteRepository = noteRepository;
        this.candidatureRepository = candidatureRepository;
        this.epreuveRepository = epreuveRepository;
    }

    public List<Note> getByCandidature(Long candidatureId) {
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new RuntimeException("Candidature introuvable"));

        return noteRepository.findByCandidature(candidature);
    }

    public List<Note> saveBulk(List<NoteRequest> requests) {

        List<Note> saved = new java.util.ArrayList<>();
        Set<Long> candidatureIds = new HashSet<>();

        for (NoteRequest req : requests) {

            Candidature candidature = candidatureRepository.findById(req.getCandidatureId())
                    .orElseThrow(() -> new RuntimeException("Candidature introuvable"));

            Epreuve epreuve = epreuveRepository.findById(req.getEpreuveId())
                    .orElseThrow(() -> new RuntimeException("Épreuve introuvable"));

            Note note = noteRepository.findByCandidatureAndEpreuve(candidature, epreuve)
                    .orElse(Note.builder()
                            .candidature(candidature)
                            .epreuve(epreuve)
                            .build());

            note.setValeur(req.getValeur());

            saved.add(noteRepository.save(note));
            candidatureIds.add(candidature.getId());
        }

        candidatureIds.forEach(this::calculerEtMettreAJourStatut);

        return saved;
    }

    private void calculerEtMettreAJourStatut(Long candidatureId) {

        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow();

        List<Epreuve> epreuves = epreuveRepository.findByConcours(candidature.getConcours());
        List<Note> notes = noteRepository.findByCandidature(candidature);

        // On attend que toutes les épreuves du concours aient une note pour ce candidat
        if (epreuves.isEmpty() || notes.size() < epreuves.size()) {
            return;
        }

        double sommePonderee = 0;
        double sommeCoefficients = 0;

        for (Epreuve epreuve : epreuves) {
            Note note = notes.stream()
                    .filter(n -> n.getEpreuve().getId().equals(epreuve.getId()))
                    .findFirst()
                    .orElse(null);

            if (note == null) {
                return; // sécurité, ne devrait pas arriver vu le check au-dessus
            }

            sommePonderee += note.getValeur() * epreuve.getCoefficient();
            sommeCoefficients += epreuve.getCoefficient();
        }

        double moyenne = sommePonderee / sommeCoefficients;

        candidature.setStatut(
                moyenne >= SEUIL_ADMISSION
                        ? Candidature.Statut.ADMIS
                        : Candidature.Statut.REFUSE
        );

        candidatureRepository.save(candidature);
    }
}