package com.school.gestion_etudiants.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MatiereRequest {

    @NotBlank(message = "Le nom de la matière est obligatoire")
    @Size(max = 150)
    private String nom;

    @NotNull(message = "Le coefficient est obligatoire")
    @DecimalMin(value = "0.5", message = "Le coefficient minimum est 0.5")
    @DecimalMax(value = "10.0", message = "Le coefficient maximum est 10")
    private Double coefficient;

    @NotNull(message = "La filière est obligatoire")
    private Long filiereId;
}
