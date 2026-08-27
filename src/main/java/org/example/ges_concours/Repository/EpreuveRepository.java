package org.example.ges_concours.Repository;

import org.example.ges_concours.Entity.Concours;
import org.example.ges_concours.Entity.Epreuve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpreuveRepository extends JpaRepository<Epreuve, Long> {

    public List<Epreuve> findByConcours(Concours concours);
}
