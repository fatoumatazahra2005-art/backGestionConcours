package org.example.ges_concours.Service;

import org.example.ges_concours.Entity.Concours;
import org.example.ges_concours.Entity.Epreuve;
import org.example.ges_concours.Repository.ConcoursRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ConcoursService {

    private final ConcoursRepository concoursRepository;
    public ConcoursService(ConcoursRepository concoursRepository) {
        this.concoursRepository = concoursRepository;
    }

    public List<Concours> getAll() {
        return concoursRepository.findAll();
    }

    public Optional<Concours> getById(Long id) {
        return concoursRepository.findById(id);
    }

    public Concours save(Concours concours) {
        return concoursRepository.save(concours);
    }

    public void delete(Long id) {
        concoursRepository.deleteById(id);
    }

    public List<Concours> findByNom(String nom) {
        return concoursRepository.findByNomContainingIgnoreCase(nom);
    }

    public Concours update(Long id, Concours newData) {

        Concours concours = concoursRepository.findById(id)
                .orElseThrow();

        concours.setNom(newData.getNom());
        concours.setDescription(newData.getDescription());
        concours.setDateLimite(newData.getDateLimite());
        concours.setDateDeliberation(newData.getDateDeliberation());

        return concoursRepository.save(concours);
    }


}
