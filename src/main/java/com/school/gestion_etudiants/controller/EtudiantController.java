package com.school.gestion_etudiants.controller;

import com.school.gestion_etudiants.dto.request.EtudiantRequest;
import com.school.gestion_etudiants.dto.response.ApiResponse;
import com.school.gestion_etudiants.dto.response.EtudiantResponse;
import com.school.gestion_etudiants.entity.User;
import com.school.gestion_etudiants.repository.UserRepository;
import com.school.gestion_etudiants.service.EtudiantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantService etudiantService;
    private final UserRepository userRepository;

    // ── ADMIN : créer un étudiant ─────────────────────────────────
    // POST /api/admin/etudiants
    @PostMapping("/admin/etudiants")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    public ResponseEntity<ApiResponse<EtudiantResponse>> creer(
            @Valid @RequestBody EtudiantRequest request) {
        EtudiantResponse response = etudiantService.creer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Étudiant créé avec succès"));
    }

    // ── ADMIN : modifier un étudiant ──────────────────────────────
    // PUT /api/admin/etudiants/{id}
    @PutMapping("/admin/etudiants/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    public ResponseEntity<ApiResponse<EtudiantResponse>> modifier(
            @PathVariable Long id,
            @Valid @RequestBody EtudiantRequest request) {
        EtudiantResponse response = etudiantService.modifier(id, request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Étudiant modifié avec succès"));
    }

    // ── ADMIN : supprimer un étudiant ─────────────────────────────
    // DELETE /api/admin/etudiants/{id}
    @DeleteMapping("/admin/etudiants/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable Long id) {
        etudiantService.supprimer(id);
        return ResponseEntity.ok(
                ApiResponse.success("Étudiant supprimé avec succès"));
    }

    // ── ADMIN : liste tous les étudiants avec filtres ──
    // GET /api/admin/etudiants
    // GET /api/admin/etudiants?recherche=Mouctar
    // GET /api/admin/etudiants?filiereId=1
    // GET /api/admin/etudiants?anneeId=1
    // GET /api/admin/etudiants?filiereId=1&anneeId=1
    @GetMapping("/admin/etudiants")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    public ResponseEntity<ApiResponse<List<EtudiantResponse>>> listerTous(
            @RequestParam(required = false) String recherche,
            @RequestParam(required = false) Long filiereId,
            @RequestParam(required = false) Long anneeId) {

        List<EtudiantResponse> liste;

        if (recherche != null && !recherche.isBlank()) {
            liste = etudiantService.rechercher(recherche);
        } else if (filiereId != null && anneeId != null) {
            liste = etudiantService.listerParFiliereEtAnnee(filiereId, anneeId);
        } else if (filiereId != null) {
            liste = etudiantService.listerParFiliere(filiereId);
        } else if (anneeId != null) {
            liste = etudiantService.listerParAnnee(anneeId);
        } else {
            liste = etudiantService.listerTous();
        }

        return ResponseEntity.ok(
                ApiResponse.success(liste, liste.size() + " étudiant(s) trouvé(s)"));
    }

    // ── ADMIN : trouver un étudiant par ID ───
    // GET /api/admin/etudiants/{id}
    @GetMapping("/admin/etudiants/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    public ResponseEntity<ApiResponse<EtudiantResponse>> trouverParId(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(etudiantService.trouverParId(id), "Étudiant trouvé"));
    }

    // ── ETUDIANT : consulter son propre profil ────
    // GET /api/etudiant/profil
    @GetMapping("/etudiant/profil")
    @PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
    public ResponseEntity<ApiResponse<EtudiantResponse>> monProfil(
            Authentication authentication) {

        // authentication.getName() retourne l'email
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        EtudiantResponse response = etudiantService.trouverParUserId(user.getId());

        return ResponseEntity.ok(ApiResponse.success(response, "Profil chargé"));
    }
}
