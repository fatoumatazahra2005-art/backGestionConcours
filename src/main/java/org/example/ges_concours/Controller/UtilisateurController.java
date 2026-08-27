package org.example.ges_concours.Controller;

import org.example.ges_concours.Dto.CreerUtilisateurRequest;
import org.example.ges_concours.Dto.UtilisateurResponse;
import org.example.ges_concours.Entity.Utilisateur;
import org.example.ges_concours.Repository.UtilisateurRepository;
import org.example.ges_concours.Service.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurController(
            UtilisateurService utilisateurService,
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder) {
        this.utilisateurService = utilisateurService;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll(@RequestParam(required = false) String search) {
        List<Utilisateur> resultats = utilisateurService.getAll();

        if (search != null && !search.isBlank()) {
            String terme = search.toLowerCase();
            resultats = resultats.stream()
                    .filter(u ->
                            u.getNom().toLowerCase().contains(terme) ||
                                    u.getPrenom().toLowerCase().contains(terme) ||
                                    u.getEmail().toLowerCase().contains(terme)
                    )
                    .collect(Collectors.toList());
        }

        List<UtilisateurResponse> data = resultats.stream()
                .map(UtilisateurResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("success", true, "data", data));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> creer(@RequestBody CreerUtilisateurRequest request) {

        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet email est déjà utilisé");
        }

        Utilisateur utilisateur = Utilisateur.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .role(request.getRole())
                .build();

        Utilisateur saved = utilisateurService.save(utilisateur);

        return ResponseEntity.ok(Map.of("success", true, "data", UtilisateurResponse.fromEntity(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id,
            @RequestBody Utilisateur utilisateurRequest) {

        Utilisateur updated = utilisateurService.update(id, utilisateurRequest);

        return ResponseEntity.ok(Map.of("success", true, "data", UtilisateurResponse.fromEntity(updated)));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<Map<String, Object>> changerRole(
            @PathVariable Long id,
            @RequestParam Utilisateur.Role role) {

        Utilisateur utilisateur = utilisateurService.getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        utilisateur.setRole(role);
        Utilisateur updated = utilisateurService.save(utilisateur);

        return ResponseEntity.ok(Map.of("success", true, "data", UtilisateurResponse.fromEntity(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        if (utilisateurService.getUserById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable");
        }

        utilisateurService.delete(id);
        return ResponseEntity.noContent().build();
    }
}