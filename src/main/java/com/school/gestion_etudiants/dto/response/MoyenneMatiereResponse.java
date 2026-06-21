package com.school.gestion_etudiants.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoyenneMatiereResponse {

    private Long matiereId;
    private String matiereNom;
    private Double coefficient;
    private Double moyenne;
    // Moyenne pondérée = moyenne * coefficient
    private Double moyennePonderee;
    // Mention calculée par la logique métier
    private String mention;
}
