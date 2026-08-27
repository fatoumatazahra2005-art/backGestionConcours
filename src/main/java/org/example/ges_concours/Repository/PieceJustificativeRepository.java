package org.example.ges_concours.Repository;

import org.example.ges_concours.Entity.Candidature;
import org.example.ges_concours.Entity.PieceJustificative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PieceJustificativeRepository extends JpaRepository<PieceJustificative, Long> {
    List<PieceJustificative> findByCandidature(Candidature candidature);
}