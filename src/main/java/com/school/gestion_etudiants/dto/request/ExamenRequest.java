package com.school.gestion_etudiants.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExamenRequest {

    @NotNull(message = "Le type d'examen est obligatoire")
    private Long typeExamenId;

    @NotNull(message = "La matière est obligatoire")
    private Long matiereId;

    private LocalDate dateExamen;
}
