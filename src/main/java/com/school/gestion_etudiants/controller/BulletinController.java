package com.school.gestion_etudiants.controller;

import com.school.gestion_etudiants.dto.response.ApiResponse;
import com.school.gestion_etudiants.dto.response.BulletinResponse;
import com.school.gestion_etudiants.service.BulletinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BulletinController {

    private final BulletinService bulletinService;

    // ADMIN : générer le bulletin d'un étudiant
    // POST /api/admin/bulletins/generer?etudiantId=1&anneeId=1
    @PostMapping("/admin/bulletins/generer")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    public ResponseEntity<ApiResponse<BulletinResponse>> generer(
            @RequestParam Long etudiantId,
            @RequestParam Long anneeId) {
        BulletinResponse response = bulletinService.generer(etudiantId, anneeId);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Bulletin généré avec succès"));
    }

    // ADMIN : valider un bulletin
    // PUT /api/admin/bulletins/{id}/valider
    @PutMapping("/admin/bulletins/{id}/valider")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    public ResponseEntity<ApiResponse<BulletinResponse>> valider(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        bulletinService.valider(id), "Bulletin validé avec succès"));
    }

    // ADMIN : invalider un bulletin
    // PUT /api/admin/bulletins/{id}/invalider
    @PutMapping("/admin/bulletins/{id}/invalider")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    public ResponseEntity<ApiResponse<BulletinResponse>> invalider(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        bulletinService.invalider(id), "Bulletin invalidé"));
    }

    // ADMIN : tous les bulletins d'une année
    // GET /api/admin/bulletins?anneeId=1
    @GetMapping("/admin/bulletins")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    public ResponseEntity<ApiResponse<List<BulletinResponse>>> parAnnee(
            @RequestParam Long anneeId) {
        List<BulletinResponse> liste = bulletinService.listerParAnnee(anneeId);
        return ResponseEntity.ok(
                ApiResponse.success(liste, liste.size() + " bulletin(s) trouvé(s)"));
    }

    // ADMIN & ETUDIANT : consulter un bulletin
    // GET /api/etudiant/bulletins?etudiantId=1&anneeId=1
    @GetMapping("/etudiant/bulletins")
    @PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
    public ResponseEntity<ApiResponse<BulletinResponse>> consulter(
            @RequestParam Long etudiantId,
            @RequestParam Long anneeId) {
        BulletinResponse response = bulletinService.consulter(etudiantId, anneeId);
        return ResponseEntity.ok(ApiResponse.success(response, "Bulletin chargé"));
    }

    // ADMIN & ETUDIANT : tous les bulletins d'un étudiant
    // GET /api/etudiant/bulletins/historique?etudiantId=1
    @GetMapping("/etudiant/bulletins/historique")
    @PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
    public ResponseEntity<ApiResponse<List<BulletinResponse>>> historique(
            @RequestParam Long etudiantId) {
        List<BulletinResponse> liste = bulletinService.listerParEtudiant(etudiantId);
        return ResponseEntity.ok(
                ApiResponse.success(liste, liste.size() + " bulletin(s)"));
    }

    // GET /api/admin/bulletins/{id}
    @GetMapping("/admin/bulletins/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BulletinResponse>> getBulletinById(
            @PathVariable Long id) {
        // Chercher le bulletin dans le service
        BulletinResponse response = bulletinService.trouverParId(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Bulletin trouvé"));
    }

}
