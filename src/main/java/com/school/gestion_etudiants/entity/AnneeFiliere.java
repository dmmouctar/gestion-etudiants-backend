package com.school.gestion_etudiants.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "annees_filieres",
        uniqueConstraints = @UniqueConstraint(columnNames = {"filiere_id", "annee_academique_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AnneeFiliere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filiere_id", nullable = false)
    private Filiere filiere;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annee_academique_id", nullable = false)
    private AnneeAcademique anneeAcademique;

}
