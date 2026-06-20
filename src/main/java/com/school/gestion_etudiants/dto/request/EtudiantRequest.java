package com.school.gestion_etudiants.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EtudiantRequest {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100)
    private String prenom;

    @Size(max = 20)
    private String cin;

    private LocalDate dateNaissance;

    @Size(max = 255)
    private String adresse;

    @Size(max = 20)
    private String telephone;

    @Email(message = "Format email invalide")
    private String email;

    // Email pour le compte d'authentification
    @NotBlank(message = "L'email du compte est obligatoire")
    @Email(message = "Format email invalide")
    private String emailCompte;

    @NotBlank(message = "Le mot de passe du compte est obligatoire")
    @Size(min = 6, message = "Minimum 6 caractères")
    private String motDePasse;

    private Long sexeId;

    @NotNull(message = "La filière est obligatoire")
    private Long filiereId;

    @NotNull(message = "L'année académique est obligatoire")
    private Long anneeAcademiqueId;
}
