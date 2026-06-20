package com.school.gestion_etudiants.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BulletinRequest {

    @NotNull(message = "L'étudiant est obligatoire")
    private Long etudiantId;

    @NotNull(message = "L'année académique est obligatoire")
    private Long anneeAcademiqueId;
}
