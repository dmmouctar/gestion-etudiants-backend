package com.school.gestion_etudiants.controller;

import com.school.gestion_etudiants.dto.request.ExamenRequest;
import com.school.gestion_etudiants.dto.response.ApiResponse;
import com.school.gestion_etudiants.dto.response.ExamenResponse;
import com.school.gestion_etudiants.service.ExamenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/examens")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ExamenController {

    private final ExamenService examenService;

    // POST /api/admin/examens
    @PostMapping
    public ResponseEntity<ApiResponse<ExamenResponse>> creer(
            @Valid @RequestBody ExamenRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        examenService.creer(request), "Examen créé avec succès"));
    }

    // PUT /api/admin/examens/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExamenResponse>> modifier(
            @PathVariable Long id,
            @Valid @RequestBody ExamenRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        examenService.modifier(id, request), "Examen modifié avec succès"));
    }

    // DELETE /api/admin/examens/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable Long id) {
        examenService.supprimer(id);
        return ResponseEntity.ok(ApiResponse.success("Examen supprimé avec succès"));
    }


    // GET /api/admin/examens
    @GetMapping
    public ResponseEntity<ApiResponse<List<ExamenResponse>>> listerTous(
            @RequestParam(required = false) Long matiereId) {
        List<ExamenResponse> liste = matiereId != null
                ? examenService.listerParMatiere(matiereId)
                : examenService.listerTous();
        return ResponseEntity.ok(ApiResponse.success(liste, liste.size() + " examen(s)"));
    }



    // GET /api/admin/examens/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExamenResponse>> trouverParId(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(examenService.trouverParId(id), "Examen trouvé"));
    }
}
