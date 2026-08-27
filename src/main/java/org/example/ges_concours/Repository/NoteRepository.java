package org.example.ges_concours.Repository;

import org.example.ges_concours.Entity.Candidature;
import org.example.ges_concours.Entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByCandidature(Candidature candidature);
    Optional<Note> findByCandidatureAndEpreuve(Candidature candidature, org.example.ges_concours.Entity.Epreuve epreuve);
}