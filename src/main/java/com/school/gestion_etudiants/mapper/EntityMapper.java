// ================================================================
// CHEMIN : src/main/java/com/school/gestion_etudiants/mapper/EntityMapper.java
// ================================================================
package com.school.gestion_etudiants.mapper;

import com.school.gestion_etudiants.dto.response.*;
import com.school.gestion_etudiants.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    // ============================================================
    // ETUDIANT
    // ============================================================
    public EtudiantResponse toEtudiantResponse(Etudiant e) {
        if (e == null) return null;
        return EtudiantResponse.builder()
                .id(e.getId())
                .nom(e.getNom())
                .prenom(e.getPrenom())
                .cin(e.getCin())
                .dateNaissance(e.getDateNaissance())
                .adresse(e.getAdresse())
                .telephone(e.getTelephone())
                .email(e.getEmail())
                .sexeId(e.getSexe() != null ? e.getSexe().getId() : null)
                .sexeNom(e.getSexe() != null ? e.getSexe().getNom() : null)
                .filiereId(e.getFiliere() != null ? e.getFiliere().getId() : null)
                .filiereNom(e.getFiliere() != null ? e.getFiliere().getNom() : null)
                .anneeAcademiqueId(e.getAnneeAcademique() != null ? e.getAnneeAcademique().getId() : null)
                .anneeAcademiqueNom(e.getAnneeAcademique() != null ? e.getAnneeAcademique().getNom() : null)
                .anneeNiveau(e.getAnneeAcademique() != null ? e.getAnneeAcademique().getNiveau() : null)
                .userId(e.getUser() != null ? e.getUser().getId() : null)
                .emailCompte(e.getUser() != null ? e.getUser().getEmail() : null)
                .build();
    }

    // ============================================================
    // MATIERE
    // ============================================================
    public MatiereResponse toMatiereResponse(Matiere m) {
        if (m == null) return null;
        return MatiereResponse.builder()
                .id(m.getId())
                .nom(m.getNom())
                .coefficient(m.getCoefficient())
                .filiereId(m.getFiliere() != null ? m.getFiliere().getId() : null)
                .filiereNom(m.getFiliere() != null ? m.getFiliere().getNom() : null)
                .build();
    }

    // ============================================================
    // EXAMEN
    // ============================================================
    public ExamenResponse toExamenResponse(Examen ex) {
        if (ex == null) return null;
        return ExamenResponse.builder()
                .id(ex.getId())
                .typeExamenId(ex.getTypeExamen() != null ? ex.getTypeExamen().getId() : null)
                .typeExamenNom(ex.getTypeExamen() != null ? ex.getTypeExamen().getNom() : null)
                .dateExamen(ex.getDateExamen())
                .matiereId(ex.getMatiere() != null ? ex.getMatiere().getId() : null)
                .matiereNom(ex.getMatiere() != null ? ex.getMatiere().getNom() : null)
                .matiereCoefficient(ex.getMatiere() != null ? ex.getMatiere().getCoefficient() : null)
                .build();
    }

    // ============================================================
    // NOTE
    // ============================================================
    public NoteResponse toNoteResponse(Note n) {
        if (n == null) return null;
        String nomComplet = n.getEtudiant() != null
                ? n.getEtudiant().getPrenom() + " " + n.getEtudiant().getNom()
                : null;
        return NoteResponse.builder()
                .id(n.getId())
                .etudiantId(n.getEtudiant() != null ? n.getEtudiant().getId() : null)
                .etudiantNomComplet(nomComplet)
                .examenId(n.getExamen() != null ? n.getExamen().getId() : null)
                .typeExamenNom(n.getExamen() != null && n.getExamen().getTypeExamen() != null
                        ? n.getExamen().getTypeExamen().getNom() : null)
                .matiereId(n.getExamen() != null && n.getExamen().getMatiere() != null
                        ? n.getExamen().getMatiere().getId() : null)
                .matiereNom(n.getExamen() != null && n.getExamen().getMatiere() != null
                        ? n.getExamen().getMatiere().getNom() : null)
                .valeur(n.getValeur())
                .build();
    }

    // ============================================================
    // MOYENNE MATIERE
    // ============================================================
    public MoyenneMatiereResponse toMoyenneMatiereResponse(MoyenneMatiere mm) {
        if (mm == null) return null;
        double coeff = mm.getMatiere() != null ? mm.getMatiere().getCoefficient() : 1.0;
        double moy   = mm.getMoyenne() != null ? mm.getMoyenne() : 0.0;
        return MoyenneMatiereResponse.builder()
                .matiereId(mm.getMatiere() != null ? mm.getMatiere().getId() : null)
                .matiereNom(mm.getMatiere() != null ? mm.getMatiere().getNom() : null)
                .coefficient(coeff)
                .moyenne(moy)
                .moyennePonderee(Math.round(moy * coeff * 100.0) / 100.0)
                .mention(calculerMention(moy))
                .build();
    }

    // ============================================================
    // BULLETIN COMPLET — correction du type List
    // ============================================================
    public BulletinResponse toBulletinResponse(Bulletin b) {
        if (b == null) return null;

        Etudiant et = b.getEtudiant();

        // ← CORRECTION : type explicite List<MoyenneMatiereResponse>
        List<MoyenneMatiereResponse> moyennes;
        if (b.getMoyennesMatieres() == null || b.getMoyennesMatieres().isEmpty()) {
            moyennes = new ArrayList<>();
        } else {
            moyennes = b.getMoyennesMatieres().stream()
                    .map(this::toMoyenneMatiereResponse)
                    .collect(Collectors.toList());
        }

        Validation val = b.getValidation();

        return BulletinResponse.builder()
                .id(b.getId())
                .etudiantId(et != null ? et.getId() : null)
                .etudiantNom(et != null ? et.getNom() : null)
                .etudiantPrenom(et != null ? et.getPrenom() : null)
                .etudiantCin(et != null ? et.getCin() : null)
                .filiereNom(et != null && et.getFiliere() != null ? et.getFiliere().getNom() : null)
                .anneeAcademiqueNom(b.getAnneeAcademique() != null ? b.getAnneeAcademique().getNom() : null)
                .anneeNiveau(b.getAnneeAcademique() != null ? b.getAnneeAcademique().getNiveau() : null)
                .moyennesMatieres(moyennes)
                .moyenneGenerale(b.getMoyenneGenerale())
                .mentionGenerale(calculerMention(b.getMoyenneGenerale()))
                .valide(b.getValide())
                .estValide(val != null && val.getValide())
                .validatedAt(val != null ? val.getValidatedAt() : null)
                .build();
    }

    // ============================================================
    // UTILITAIRE : calcul de mention
    // ============================================================
    public String calculerMention(Double moyenne) {
        if (moyenne == null) return "Non évalué";
        if (moyenne >= 16) return "Très Bien";
        if (moyenne >= 14) return "Bien";
        if (moyenne >= 12) return "Assez Bien";
        if (moyenne >= 10) return "Passable";
        return "Insuffisant";
    }
}
