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
@RequestMapping("/api/admin/annees")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AnneeAcademiqueController {

    private final AnneeAcademiqueRepository anneeRepository;

    @PostMapping
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

    @GetMapping
    public ResponseEntity<ApiResponse<List<AnneeAcademique>>> lister() {
        List<AnneeAcademique> liste = anneeRepository.findAll();
        return ResponseEntity.ok(
                ApiResponse.success(liste, liste.size() + " année(s)"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AnneeAcademique>> trouver(@PathVariable Long id) {
        AnneeAcademique annee = anneeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Année non trouvée"));
        return ResponseEntity.ok(ApiResponse.success(annee, "Année trouvée"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AnneeAcademique>> modifier(
            @PathVariable Long id, @RequestBody AnneeAcademique request) {
        AnneeAcademique annee = anneeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Année non trouvée"));
        annee.setNom(request.getNom());
        annee.setNiveau(request.getNiveau());
        return ResponseEntity.ok(
                ApiResponse.success(anneeRepository.save(annee), "Année modifiée"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable Long id) {
        if (!anneeRepository.existsById(id)) {
            throw new RuntimeException("Année non trouvée");
        }
        anneeRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Année supprimée"));
    }
}