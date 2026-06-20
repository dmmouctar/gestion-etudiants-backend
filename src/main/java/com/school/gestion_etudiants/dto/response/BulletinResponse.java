package com.school.gestion_etudiants.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulletinResponse {

    private Long id;

    // Infos étudiant
    private Long etudiantId;
    private String etudiantNom;
    private String etudiantPrenom;
    private String etudiantCin;
    private String filiereNom;
    private String anneeAcademiqueNom;
    private Integer anneeNiveau;

    // Résultats
    private List<MoyenneMatiereResponse> moyennesMatieres;
    private Double moyenneGenerale;
    private String mentionGenerale;
    private Boolean valide;

    // Validation
    private Boolean estValide;
    private LocalDateTime validatedAt;
}
