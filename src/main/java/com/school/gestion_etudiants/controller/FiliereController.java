package com.school.gestion_etudiants.controller;

import com.school.gestion_etudiants.dto.response.ApiResponse;
import com.school.gestion_etudiants.entity.Filiere;
import com.school.gestion_etudiants.repository.FiliereRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/filieres")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class FiliereController {

    private final FiliereRepository filiereRepository;

    // POST /api/admin/filieres
    @PostMapping
    public ResponseEntity<ApiResponse<Filiere>> creer(@RequestBody Filiere filiere) {
        if (filiereRepository.existsByNom(filiere.getNom())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Cette filière existe déjà"));
        }
        Filiere saved = filiereRepository.save(filiere);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(saved, "Filière créée avec succès"));
    }

    // GET /api/admin/filieres
    @GetMapping
    public ResponseEntity<ApiResponse<List<Filiere>>> lister() {
        List<Filiere> liste = filiereRepository.findAll();
        return ResponseEntity.ok(
                ApiResponse.success(liste, liste.size() + " filière(s)"));
    }

    // GET /api/admin/filieres/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Filiere>> trouver(@PathVariable Long id) {
        Filiere filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filière non trouvée"));
        return ResponseEntity.ok(ApiResponse.success(filiere, "Filière trouvée"));
    }

    // PUT /api/admin/filieres/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Filiere>> modifier(
            @PathVariable Long id, @RequestBody Filiere request) {
        Filiere filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filière non trouvée"));
        filiere.setNom(request.getNom());
        filiere.setDescription(request.getDescription());
        return ResponseEntity.ok(
                ApiResponse.success(filiereRepository.save(filiere), "Filière modifiée"));
    }

    // DELETE /api/admin/filieres/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable Long id) {
        if (!filiereRepository.existsById(id)) {
            throw new RuntimeException("Filière non trouvée");
        }
        filiereRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Filière supprimée"));
    }
}
