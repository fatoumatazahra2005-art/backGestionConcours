package org.example.ges_concours.Controller;

import org.example.ges_concours.Dto.EpreuveRequest;
import org.example.ges_concours.Entity.Epreuve;
import org.example.ges_concours.Service.EpreuveService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/epreuves")
public class EpreuveController {
    private final EpreuveService epreuveService;
    public EpreuveController(EpreuveService epreuveService) {
        this.epreuveService = epreuveService;
    }

    @GetMapping
    public List<Epreuve> findAll() {
        return epreuveService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Epreuve> findById(@PathVariable Long id) {
        return epreuveService.findById(id);
    }

    @PostMapping
    public Epreuve save(@RequestBody EpreuveRequest request) {
        return epreuveService.creer(request);
    }

    @PutMapping("/{id}")
    public Epreuve update(
            @PathVariable Long id,
            @RequestBody EpreuveRequest request
    ) {
        return epreuveService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        epreuveService.delete(id);
    }

    @GetMapping("/concours/{concoursId}")
    public List<Epreuve> findByConcours(@PathVariable Long concoursId) {
        return epreuveService.findByConcours(concoursId);
    }
}