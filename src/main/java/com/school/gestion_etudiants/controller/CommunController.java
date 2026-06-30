package com.school.gestion_etudiants.controller;

import com.school.gestion_etudiants.dto.response.ApiResponse;
import com.school.gestion_etudiants.dto.response.ExamenResponse;
import com.school.gestion_etudiants.dto.response.MatiereResponse;
import com.school.gestion_etudiants.entity.AnneeAcademique;
import com.school.gestion_etudiants.repository.AnneeAcademiqueRepository;
import com.school.gestion_etudiants.service.ExamenService;
import com.school.gestion_etudiants.service.MatiereService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
public class CommunController {

    private final MatiereService matiereService;
    private final ExamenService examenService;
    private final AnneeAcademiqueRepository anneeRepository;

    // GET /api/matieres?filiereId=1
    @GetMapping("/matieres")
    public ResponseEntity<ApiResponse<List<MatiereResponse>>> listerMatieres(
            @RequestParam(required = false) Long filiereId) {
        List<MatiereResponse> liste = filiereId != null
                ? matiereService.listerParFiliere(filiereId)
                : matiereService.listerTous();
        return ResponseEntity.ok(ApiResponse.success(liste, liste.size() + " matière(s)"));
    }

    // GET /api/examens?matiereId=1
    @GetMapping("/examens")
    public ResponseEntity<ApiResponse<List<ExamenResponse>>> listerExamens(
            @RequestParam(required = false) Long matiereId) {
        List<ExamenResponse> liste = matiereId != null
                ? examenService.listerParMatiere(matiereId)
                : examenService.listerTous();
        return ResponseEntity.ok(ApiResponse.success(liste, liste.size() + " examen(s)"));
    }

    // GET /api/annees
    @GetMapping("/annees")
    public ResponseEntity<ApiResponse<List<AnneeAcademique>>> listerAnnees() {
        List<AnneeAcademique> liste = anneeRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(liste, liste.size() + " année(s)"));
    }
}
