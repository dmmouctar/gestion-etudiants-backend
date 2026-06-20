package com.school.gestion_etudiants.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NoteRequest {

    @NotNull(message = "L'étudiant est obligatoire")
    private Long etudiantId;

    @NotNull(message = "L'examen est obligatoire")
    private Long examenId;

    @NotNull(message = "La valeur est obligatoire")
    @DecimalMin(value = "0.0", message = "La note minimum est 0")
    @DecimalMax(value = "20.0", message = "La note maximum est 20")
    private Double valeur;
}
