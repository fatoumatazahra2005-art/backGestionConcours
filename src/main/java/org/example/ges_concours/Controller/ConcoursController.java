package org.example.ges_concours.Controller;

import org.example.ges_concours.Entity.Concours;
import org.example.ges_concours.Repository.ConcoursRepository;
import org.example.ges_concours.Service.ConcoursService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/concours")
public class ConcoursController {

    private final ConcoursService concoursService;
    public ConcoursController(ConcoursService concoursService) {
        this.concoursService= concoursService;
    }

    @GetMapping
    public List<Concours> findAll() {
        return concoursService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Concours> getById(@PathVariable Long id){
        return concoursService.getById(id);

    }

    @PostMapping
    public Concours save(@RequestBody Concours concours) {
        return concoursService.save(concours);
    }
    @DeleteMapping("/{id}")
    public void  delete(@PathVariable Long id) {
        concoursService.delete(id);
    }

    @GetMapping("/search")
    public List<Concours> search(@RequestParam String nom){
        return concoursService.findByNom(nom);
    }

    @PutMapping("/{id}")
    public Concours update(
            @PathVariable Long id,
            @RequestBody Concours concours
    ) {
        return concoursService.update(id,concours);
    }
}
