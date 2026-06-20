package com.school.gestion_etudiants.controller;

import com.school.gestion_etudiants.dto.request.NoteRequest;
import com.school.gestion_etudiants.dto.response.ApiResponse;
import com.school.gestion_etudiants.dto.response.NoteResponse;
import com.school.gestion_etudiants.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class NoteController {

    private final NoteService noteService;

    // POST /api/admin/notes  (crée ou met à jour)
    @PostMapping
    public ResponseEntity<ApiResponse<NoteResponse>> saisir(
            @Valid @RequestBody NoteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        noteService.saisir(request), "Note saisie avec succès"));
    }

    // DELETE /api/admin/notes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable Long id) {
        noteService.supprimer(id);
        return ResponseEntity.ok(ApiResponse.success("Note supprimée avec succès"));
    }

    // GET /api/admin/notes/etudiant/{etudiantId}
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<ApiResponse<List<NoteResponse>>> parEtudiant(
            @PathVariable Long etudiantId) {
        List<NoteResponse> liste = noteService.listerParEtudiant(etudiantId);
        return ResponseEntity.ok(
                ApiResponse.success(liste, liste.size() + " note(s) trouvée(s)"));
    }

    // GET /api/admin/notes/examen/{examenId}
    @GetMapping("/examen/{examenId}")
    public ResponseEntity<ApiResponse<List<NoteResponse>>> parExamen(
            @PathVariable Long examenId) {
        List<NoteResponse> liste = noteService.listerParExamen(examenId);
        return ResponseEntity.ok(
                ApiResponse.success(liste, liste.size() + " note(s) trouvée(s)"));
    }
}
