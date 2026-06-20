package com.school.gestion_etudiants.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {

    private Long id;
    private Long etudiantId;
    private String etudiantNomComplet;
    private Long examenId;
    private String typeExamenNom;
    private Long matiereId;
    private String matiereNom;
    private Double valeur;
}
