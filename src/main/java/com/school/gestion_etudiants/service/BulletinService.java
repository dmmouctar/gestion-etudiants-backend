package com.school.gestion_etudiants.service;

import com.school.gestion_etudiants.dto.response.BulletinResponse;
import com.school.gestion_etudiants.entity.*;
import com.school.gestion_etudiants.mapper.EntityMapper;
import com.school.gestion_etudiants.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BulletinService {

    private final BulletinRepository bulletinRepository;
    private final EtudiantRepository etudiantRepository;
    private final AnneeAcademiqueRepository anneeAcademiqueRepository;
    private final MatiereRepository matiereRepository;
    private final NoteRepository noteRepository;
    private final MoyenneMatiereRepository moyenneMatiereRepository;
    private final ValidationRepository validationRepository;
    private final EntityMapper mapper;

    // ── Générer ou recalculer le bulletin d'un étudiant ──────────
    @Transactional
    public BulletinResponse generer(Long etudiantId, Long anneeAcademiqueId) {

        Etudiant etudiant = etudiantRepository.findByIdWithDetails(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé : " + etudiantId));

        AnneeAcademique annee = anneeAcademiqueRepository.findById(anneeAcademiqueId)
                .orElseThrow(() -> new RuntimeException("Année académique non trouvée"));

        Bulletin bulletin = bulletinRepository
                .findByEtudiantIdAndAnneeAcademiqueId(etudiantId, anneeAcademiqueId)
                .orElse(Bulletin.builder()
                        .etudiant(etudiant)
                        .anneeAcademique(annee)
                        .valide(false)
                        .build());

        bulletin = bulletinRepository.save(bulletin);

        List<Matiere> matieres = matiereRepository.findByFiliereId(
                etudiant.getFiliere().getId()
        );

        double sommeNotesPonderees = 0.0;
        double sommeCoefficients   = 0.0;
        List<MoyenneMatiere> moyennesMatieres = new ArrayList<>();

        for (Matiere matiere : matieres) {
            Double moyenneMatiere = noteRepository
                    .calculerMoyenneParMatiere(etudiantId, matiere.getId())
                    .orElse(null);

            MoyenneMatiere mm = moyenneMatiereRepository
                    .findByBulletinIdAndMatiereId(bulletin.getId(), matiere.getId())
                    .orElse(MoyenneMatiere.builder()
                            .bulletin(bulletin)
                            .matiere(matiere)
                            .build());

            mm.setMoyenne(moyenneMatiere);
            mm = moyenneMatiereRepository.save(mm);
            moyennesMatieres.add(mm);

            if (moyenneMatiere != null) {
                sommeNotesPonderees += moyenneMatiere * matiere.getCoefficient();
                sommeCoefficients   += matiere.getCoefficient();
            }
        }

        Double moyenneGenerale = null;
        if (sommeCoefficients > 0) {
            moyenneGenerale = Math.round(
                    (sommeNotesPonderees / sommeCoefficients) * 100.0
            ) / 100.0;
        }

        bulletin.setMoyenneGenerale(moyenneGenerale);
        bulletin.setMoyennesMatieres(moyennesMatieres);
        bulletin = bulletinRepository.save(bulletin);

        bulletin = bulletinRepository
                .findWithDetailsByEtudiantAndAnnee(etudiantId, anneeAcademiqueId)
                .orElse(bulletin);

        return mapper.toBulletinResponse(bulletin);
    }

    // ── Valider un bulletin ───────────────────────────────────────
    @Transactional
    public BulletinResponse valider(Long bulletinId) {
        Bulletin bulletin = bulletinRepository.findById(bulletinId)
                .orElseThrow(() -> new RuntimeException("Bulletin non trouvé : " + bulletinId));

        if (bulletin.getMoyenneGenerale() == null) {
            throw new RuntimeException("Impossible de valider : aucune moyenne calculée");
        }

        bulletin.setValide(true);
        bulletin = bulletinRepository.save(bulletin);

        Validation validation = validationRepository
                .findByBulletinId(bulletinId)
                .orElse(Validation.builder()
                        .bulletin(bulletin)
                        .build());

        validation.setValide(true);
        validation.setValidatedAt(LocalDateTime.now());
        validationRepository.save(validation);

        bulletin = bulletinRepository
                .findWithDetailsByEtudiantAndAnnee(
                        bulletin.getEtudiant().getId(),
                        bulletin.getAnneeAcademique().getId()
                )
                .orElse(bulletin);

        return mapper.toBulletinResponse(bulletin);
    }

    // ── Invalider un bulletin ─────────────────────────────────────
    @Transactional
    public BulletinResponse invalider(Long bulletinId) {
        Bulletin bulletin = bulletinRepository.findById(bulletinId)
                .orElseThrow(() -> new RuntimeException("Bulletin non trouvé : " + bulletinId));

        bulletin.setValide(false);
        bulletin = bulletinRepository.save(bulletin);

        validationRepository.findByBulletinId(bulletinId).ifPresent(val -> {
            val.setValide(false);
            val.setValidatedAt(null);
            validationRepository.save(val);
        });

        return mapper.toBulletinResponse(bulletin);
    }

    // ── Consulter un bulletin ─────────────────────────────────────
    @Transactional
    public BulletinResponse consulter(Long etudiantId, Long anneeAcademiqueId) {
        Bulletin bulletin = bulletinRepository
                .findWithDetailsByEtudiantAndAnnee(etudiantId, anneeAcademiqueId)
                .orElseThrow(() -> new RuntimeException("Bulletin non trouvé"));
        return mapper.toBulletinResponse(bulletin);
    }

    // ── Trouver un bulletin par ID ────────────────────────────────
    @Transactional
    public BulletinResponse trouverParId(Long id) {
        Bulletin bulletin = bulletinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bulletin non trouvé : " + id));
        return mapper.toBulletinResponse(bulletin);
    }

    // ── Tous les bulletins d'un étudiant ──────────────────────────
    @Transactional  // ← CORRECTION
    public List<BulletinResponse> listerParEtudiant(Long etudiantId) {
        return bulletinRepository.findByEtudiantId(etudiantId)
                .stream()
                .map(mapper::toBulletinResponse)
                .collect(Collectors.toList());
    }

    // ── Tous les bulletins d'une année ────────────────────────────
    @Transactional
    public List<BulletinResponse> listerParAnnee(Long anneeId) {
        return bulletinRepository.findByAnneeAcademiqueId(anneeId)
                .stream()
                .map(mapper::toBulletinResponse)
                .collect(Collectors.toList());
    }

}