package org.example.ges_concours.Repository;

import org.example.ges_concours.Entity.Candidature;
import org.example.ges_concours.Entity.Concours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultatRepository extends JpaRepository<Candidature, Long> {

    List<Candidature> findByStatut(Candidature.Statut statut);

    List<Candidature> findByConcoursAndStatut(
            Concours concours,
            Candidature.Statut statut
    );

    List<Candidature> findByConcoursAndStatutIn(
            Concours concours,
            List<Candidature.Statut> statuts
    );

    List<Candidature> findByStatutIn(
            List<Candidature.Statut> statuts
    );
}