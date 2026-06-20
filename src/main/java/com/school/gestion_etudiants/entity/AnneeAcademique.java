package com.school.gestion_etudiants.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "annees_academiques")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AnneeAcademique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nom;

    @Column(nullable = false)
    private Integer niveau;

    @JsonIgnore
    @OneToMany(mappedBy = "anneeAcademique", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AnneeFiliere> anneeFilieres;
}
