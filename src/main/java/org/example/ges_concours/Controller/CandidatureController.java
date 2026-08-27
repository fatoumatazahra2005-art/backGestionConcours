package org.example.ges_concours.Controller;

import org.example.ges_concours.Dto.CandidatureRequest;
import org.example.ges_concours.Dto.CandidatureResponse;
import org.example.ges_concours.Entity.Candidature;
import org.example.ges_concours.Service.CandidatureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/candidatures")
public class CandidatureController {

    private final CandidatureService candidatureService;

    public CandidatureController(CandidatureService candidatureService) {
        this.candidatureService = candidatureService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll() {
        List<CandidatureResponse> data = candidatureService.getAll().stream()
                .map(CandidatureResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("success", true, "data", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        Candidature candidature = candidatureService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidature introuvable"));

        return ResponseEntity.ok(Map.of("success", true, "data", CandidatureResponse.fromEntity(candidature)));
    }

    @GetMapping("/candidat/{id}")
    public ResponseEntity<Map<String, Object>> getByCandidat(@PathVariable Long id) {
        List<CandidatureResponse> data = candidatureService.getByCandidat(id).stream()
                .map(CandidatureResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("success", true, "data", data));
    }

    @GetMapping("/concours/{id}")
    public ResponseEntity<Map<String, Object>> getByConcours(@PathVariable Long id) {
        List<CandidatureResponse> data = candidatureService.getByConcours(id).stream()
                .map(CandidatureResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("success", true, "data", data));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> creer(
            @RequestBody CandidatureRequest request,
            Authentication authentication) {

        String email = authentication.getName();
        Candidature candidature = candidatureService.creer(request.getConcoursId(), email);

        return ResponseEntity.ok(Map.of("success", true, "data", CandidatureResponse.fromEntity(candidature)));
    }

    @GetMapping("/mes-candidatures")
    public ResponseEntity<Map<String, Object>> mesCandidatures(Authentication authentication) {
        String email = authentication.getName();

        List<CandidatureResponse> data = candidatureService.getByEmail(email).stream()
                .map(CandidatureResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("success", true, "data", data));
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<Map<String, Object>> changerStatut(
            @PathVariable Long id,
            @RequestParam Candidature.Statut statut) {

        Candidature candidature = candidatureService.changerStatut(id, statut);

        return ResponseEntity.ok(Map.of("success", true, "data", CandidatureResponse.fromEntity(candidature)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        candidatureService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/annuler")
    public ResponseEntity<Void> annuler(@PathVariable Long id, Authentication authentication) {
        candidatureService.annulerParCandidat(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }


}