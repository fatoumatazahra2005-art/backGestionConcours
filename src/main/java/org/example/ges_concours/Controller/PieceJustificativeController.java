package org.example.ges_concours.Controller;

import org.example.ges_concours.Entity.PieceJustificative;
import org.example.ges_concours.Service.PieceJustificativeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/candidatures/{candidatureId}/pieces")
public class PieceJustificativeController {

    private final PieceJustificativeService pieceService;

    public PieceJustificativeController(PieceJustificativeService pieceService) {
        this.pieceService = pieceService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> ajouter(
            @PathVariable Long candidatureId,
            @RequestParam("type") PieceJustificative.TypePiece type,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        PieceJustificative piece = pieceService.ajouter(
                candidatureId, type, file, authentication.getName()
        );

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                        "id", piece.getId(),
                        "type", piece.getType(),
                        "url", piece.getCheminFichier(),
                        "nomOriginal", piece.getNomOriginal()
                )
        ));
    }

    @DeleteMapping("/{pieceId}")
    public ResponseEntity<Map<String, Object>> supprimer(
            @PathVariable Long candidatureId,
            @PathVariable Long pieceId,
            Authentication authentication) {

        pieceService.supprimer(candidatureId, pieceId, authentication.getName());

        return ResponseEntity.ok(Map.of("success", true));
    }
}