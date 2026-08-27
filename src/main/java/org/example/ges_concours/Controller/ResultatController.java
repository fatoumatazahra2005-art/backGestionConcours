package org.example.ges_concours.Controller;

import org.example.ges_concours.Dto.ResultatResponse;
import org.example.ges_concours.Entity.Candidature;
import org.example.ges_concours.Service.ResultatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resultats")
public class ResultatController {

    private final ResultatService resultatService;

    public ResultatController(ResultatService resultatService) {
        this.resultatService = resultatService;
    }


    @GetMapping
    public ResponseEntity<List<ResultatResponse>> getResultats() {

        return ResponseEntity.ok(
                resultatService.getResultats()
        );
    }

    @GetMapping("/refuses")
    public ResponseEntity<List<ResultatResponse>> getRefuses() {

        return ResponseEntity.ok(
                resultatService.getRefuses()
        );
    }


    @GetMapping("/admis")
    public ResponseEntity<List<ResultatResponse>> getAdmis() {

        return ResponseEntity.ok(
                resultatService.getAdmis()
        );
    }


    @GetMapping("/liste-attente")
    public ResponseEntity<List<ResultatResponse>> getListeAttente() {

        return ResponseEntity.ok(
                resultatService.getListeAttente()
        );
    }


    @GetMapping("/concours/{concoursId}")
    public ResponseEntity<List<Candidature>> getResultatsByConcours(
            @PathVariable Long concoursId) {

        return ResponseEntity.ok(
                resultatService.getResultatsByConcours(concoursId)
        );
    }
}