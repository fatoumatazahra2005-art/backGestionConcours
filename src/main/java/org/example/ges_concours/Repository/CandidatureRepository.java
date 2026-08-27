package org.example.ges_concours.Repository;

import org.example.ges_concours.Entity.Candidature;
import org.example.ges_concours.Entity.Concours;
import org.example.ges_concours.Entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    List<Candidature> findByCandidat(Utilisateur candidat);
    List<Candidature> findByConcours(Concours concours);
    Optional<Candidature> findByCandidatAndConcours(Utilisateur candidat, Concours concours);

    @Query("SELECT DISTINCT c FROM Candidature c LEFT JOIN FETCH c.pieces WHERE c.candidat = :candidat")
    List<Candidature> findByCandidatWithPieces(Utilisateur candidat);

    @Query("SELECT DISTINCT c FROM Candidature c LEFT JOIN FETCH c.pieces WHERE c.concours = :concours")
    List<Candidature> findByConcoursWithPieces(Concours concours);

    @Query("SELECT DISTINCT c FROM Candidature c LEFT JOIN FETCH c.pieces")
    List<Candidature> findAllWithPieces();

    @Query("SELECT c FROM Candidature c LEFT JOIN FETCH c.pieces WHERE c.id = :id")
    Optional<Candidature> findByIdWithPieces(Long id);
}