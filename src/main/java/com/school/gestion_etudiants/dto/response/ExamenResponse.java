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
public class ExamenResponse {

    private Long id;
    private String typeExamenNom;
    private Long typeExamenId;
    private LocalDate dateExamen;
    private Long matiereId;
    private String matiereNom;
    private Double matiereCoefficient;
}
