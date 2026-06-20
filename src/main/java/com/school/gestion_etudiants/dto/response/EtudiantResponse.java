package com.school.gestion_etudiants.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantResponse {

    private Long id;
    private String nom;
    private String prenom;
    private String cin;
    private LocalDate dateNaissance;
    private String adresse;
    private String telephone;
    private String email;

    // Informations liées (on évite les objets imbriqués profonds)
    private Long sexeId;
    private String sexeNom;

    private Long filiereId;
    private String filiereNom;

    private Long anneeAcademiqueId;
    private String anneeAcademiqueNom;
    private Integer anneeNiveau;

    // Compte utilisateur lié
    private Long userId;
    private String emailCompte;
}
