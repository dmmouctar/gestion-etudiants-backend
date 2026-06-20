package com.school.gestion_etudiants.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

@Entity
@Table(name = "notes", uniqueConstraints = @UniqueConstraint(
        columnNames = {"etudiant_id", "examen_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examen_id", nullable = false)
    private Examen examen;

    @DecimalMin("0.0")
    @DecimalMax("20.0")
    @Column(nullable = false)
    private Double valeur;
}
