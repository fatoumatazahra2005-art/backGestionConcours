package org.example.ges_concours.Repository;

import org.example.ges_concours.Entity.Concours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcoursRepository extends JpaRepository<Concours, Long> {
    public List<Concours> findByNomContainingIgnoreCase(String nom);
}
