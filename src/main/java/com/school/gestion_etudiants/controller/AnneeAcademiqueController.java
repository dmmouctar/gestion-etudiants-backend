package com.school.gestion_etudiants.controller;

import com.school.gestion_etudiants.dto.response.ApiResponse;
import com.school.gestion_etudiants.entity.AnneeAcademique;
import com.school.gestion_etudiants.repository.AnneeAcademiqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnneeAcademiqueController {

    private final AnneeAcademiqueRepository anneeRepository;

    // ── Route ADMIN : gestion complète ───────────────────────────
    // POST /api/admin/annees
    @PostMapping("/api/admin/annees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AnneeAcademique>> creer(
            @RequestBody AnneeAcademique annee) {
        if (anneeRepository.existsByNom(annee.getNom())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Cette année académique existe déjà"));
        }
        AnneeAcademique saved = anneeRepository.save(annee);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(saved, "Année académique créée avec succès"));
    }

    // GET /api/admin/annees (admin)
    @GetMapping("/api/admin/annees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AnneeAcademique>>> listerAdmin() {
        List<AnneeAcademique> liste = anneeRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(liste, liste.size() + " année(s)"));
    }

    // GET /api/admin/annees/{id}
    @GetMapping("/api/admin/annees/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AnneeAcademique>> trouver(@PathVariable Long id) {
        AnneeAcademique annee = anneeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Année non trouvée"));
        return ResponseEntity.ok(ApiResponse.success(annee, "Année trouvée"));
    }

    // PUT /api/admin/annees/{id}
    @PutMapping("/api/admin/annees/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AnneeAcademique>> modifier(
            @PathVariable Long id, @RequestBody AnneeAcademique request) {
        AnneeAcademique annee = anneeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Année non trouvée"));
        annee.setNom(request.getNom());
        annee.setNiveau(request.getNiveau());
        return ResponseEntity.ok(
                ApiResponse.success(anneeRepository.save(annee), "Année modifiée"));
    }

    // DELETE /api/admin/annees/{id}
    @DeleteMapping("/api/admin/annees/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable Long id) {
        if (!anneeRepository.existsById(id))
            throw new RuntimeException("Année non trouvée");
        anneeRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Année supprimée"));
    }

    // ── Route COMMUNE : accessible par ADMIN et ETUDIANT ─────────
    // GET /api/annees  ← nouvelle route sans "admin"
    @GetMapping("/api/annees")
    @PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
    public ResponseEntity<ApiResponse<List<AnneeAcademique>>> listerTous() {
        List<AnneeAcademique> liste = anneeRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(liste, liste.size() + " année(s)"));
    }
}