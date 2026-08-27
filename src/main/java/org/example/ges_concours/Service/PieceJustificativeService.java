package org.example.ges_concours.Service;

import org.example.ges_concours.Entity.Candidature;
import org.example.ges_concours.Entity.PieceJustificative;
import org.example.ges_concours.Repository.CandidatureRepository;
import org.example.ges_concours.Repository.PieceJustificativeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class PieceJustificativeService {

    private final PieceJustificativeRepository pieceRepository;
    private final CandidatureRepository candidatureRepository;

    @Value("${app.upload-dir}")
    private String uploadDir;

    public PieceJustificativeService(
            PieceJustificativeRepository pieceRepository,
            CandidatureRepository candidatureRepository) {
        this.pieceRepository = pieceRepository;
        this.candidatureRepository = candidatureRepository;
    }

    public PieceJustificative ajouter(
            Long candidatureId,
            PieceJustificative.TypePiece type,
            MultipartFile file,
            String emailConnecte) {

        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidature introuvable"));

        if (!candidature.getCandidat().getEmail().equals(emailConnecte)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cette candidature ne vous appartient pas");
        }

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fichier vide");
        }

        try {
            Path dossier = Paths.get(uploadDir, "candidatures", candidatureId.toString());
            Files.createDirectories(dossier);

            String extension = extraireExtension(file.getOriginalFilename());
            String nomFichier = UUID.randomUUID() + extension;

            Path destination = dossier.resolve(nomFichier);
            Files.copy(file.getInputStream(), destination);

            PieceJustificative piece = PieceJustificative.builder()
                    .type(type)
                    .cheminFichier("/uploads/candidatures/" + candidatureId + "/" + nomFichier)
                    .nomOriginal(file.getOriginalFilename())
                    .candidature(candidature)
                    .build();

            return pieceRepository.save(piece);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Échec de l'enregistrement du fichier");
        }
    }

    private String extraireExtension(String nomFichier) {
        if (nomFichier == null || !nomFichier.contains(".")) return "";
        return nomFichier.substring(nomFichier.lastIndexOf('.'));
    }

    public void supprimer(Long candidatureId, Long pieceId, String emailConnecte) {

        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidature introuvable"));

        if (!candidature.getCandidat().getEmail().equals(emailConnecte)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cette candidature ne vous appartient pas");
        }

        PieceJustificative piece = pieceRepository.findById(pieceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document introuvable"));

        if (!piece.getCandidature().getId().equals(candidatureId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce document n'appartient pas à cette candidature");
        }

        try {
            Path fichier = Paths.get(uploadDir, piece.getCheminFichier().replaceFirst("^/uploads/", ""));
            Files.deleteIfExists(fichier);
        } catch (IOException e) {
            // on continue même si le fichier physique n'a pas pu être supprimé (pas bloquant)
        }

        pieceRepository.delete(piece);
    }
}