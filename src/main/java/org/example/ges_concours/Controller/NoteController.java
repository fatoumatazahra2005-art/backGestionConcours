package org.example.ges_concours.Controller;

import org.example.ges_concours.Dto.NoteRequest;
import org.example.ges_concours.Entity.Note;
import org.example.ges_concours.Service.NoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/candidature/{candidatureId}")
    public List<Note> getByCandidature(@PathVariable Long candidatureId) {
        return noteService.getByCandidature(candidatureId);
    }

    @PostMapping("/bulk")
    public List<Note> saveBulk(@RequestBody List<NoteRequest> requests) {
        return noteService.saveBulk(requests);
    }
}