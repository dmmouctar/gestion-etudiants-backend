package com.school.gestion_etudiants.controller;

import com.school.gestion_etudiants.dto.request.MatiereRequest;
import com.school.gestion_etudiants.dto.response.ApiResponse;
import com.school.gestion_etudiants.dto.response.MatiereResponse;
import com.school.gestion_etudiants.service.MatiereService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/matieres")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class MatiereController {

    private final MatiereService matiereService;

    // POST /api/admin/matieres
    @PostMapping
    public ResponseEntity<ApiResponse<MatiereResponse>> creer(
            @Valid @RequestBody MatiereRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        matiereService.creer(request), "Matière créée avec succès"));
    }

    // PUT /api/admin/matieres/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MatiereResponse>> modifier(
            @PathVariable Long id,
            @Valid @RequestBody MatiereRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        matiereService.modifier(id, request), "Matière modifiée avec succès"));
    }

    // DELETE /api/admin/matieres/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable Long id) {
        matiereService.supprimer(id);
        return ResponseEntity.ok(ApiResponse.success("Matière supprimée avec succès"));
    }


    // GET /api/admin/matieres
    @GetMapping
    public ResponseEntity<ApiResponse<List<MatiereResponse>>> listerTous(
            @RequestParam(required = false) Long filiereId) {
        List<MatiereResponse> liste = filiereId != null
                ? matiereService.listerParFiliere(filiereId)
                : matiereService.listerTous();
        return ResponseEntity.ok(ApiResponse.success(liste, liste.size() + " matière(s)"));
    }



    // GET /api/admin/matieres/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MatiereResponse>> trouverParId(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(matiereService.trouverParId(id), "Matière trouvée"));
    }
}
