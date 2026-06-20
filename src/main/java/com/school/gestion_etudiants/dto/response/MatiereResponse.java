package com.school.gestion_etudiants.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatiereResponse {

    private Long id;
    private String nom;
    private Double coefficient;
    private Long filiereId;
    private String filiereNom;
}
