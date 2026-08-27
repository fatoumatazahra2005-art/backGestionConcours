package org.example.ges_concours.Service;

import org.example.ges_concours.Dto.EpreuveRequest;
import org.example.ges_concours.Entity.Concours;
import org.example.ges_concours.Entity.Epreuve;
import org.example.ges_concours.Repository.ConcoursRepository;
import org.example.ges_concours.Repository.EpreuveRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class EpreuveService {
    private final EpreuveRepository epreuveRepository;
    private final ConcoursRepository concoursRepository;

    public EpreuveService(EpreuveRepository epreuveRepository , ConcoursRepository   concoursRepository) {
        this.epreuveRepository = epreuveRepository;
        this.concoursRepository = concoursRepository;
    }

    public List<Epreuve> findAll() {
        return epreuveRepository.findAll();
    }

    public Optional<Epreuve> findById(Long id) {
        return epreuveRepository.findById(id);
    }

    public Epreuve creer(EpreuveRequest request) {
        Concours concours = concoursRepository.findById(request.getConcoursId())
                .orElseThrow(() -> new RuntimeException("Concours introuvable"));

        Epreuve epreuve = new Epreuve();
        epreuve.setNom(request.getNom());
        epreuve.setCoefficient(request.getCoefficient());
        epreuve.setDuree(request.getDuree());
        epreuve.setConcours(concours);

        return epreuveRepository.save(epreuve);
    }

    public void delete(Long id) {
        epreuveRepository.deleteById(id);
    }

    public Epreuve update(Long id, EpreuveRequest request) {

        Epreuve epreuve = epreuveRepository.findById(id)
                .orElseThrow();

        epreuve.setNom(request.getNom());
        epreuve.setCoefficient(request.getCoefficient());
        epreuve.setDuree(request.getDuree());
        // pas de setConcours() ici : on ne change jamais le concours d'une épreuve existante

        return epreuveRepository.save(epreuve);
    }

    public List<Epreuve> findByConcours(Long concoursId) {
        Concours concours = concoursRepository.findById(concoursId)
                .orElseThrow(() -> new RuntimeException("Concours introuvable"));

        return epreuveRepository.findByConcours(concours);
    }
}