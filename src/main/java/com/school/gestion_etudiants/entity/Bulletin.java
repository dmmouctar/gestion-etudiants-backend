package com.school.gestion_etudiants.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "bulletins", uniqueConstraints = @UniqueConstraint(columnNames = {
        "etudiant_id", "annee_academique_id"
}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bulletin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annee_academique_id", nullable = false)
    private AnneeAcademique anneeAcademique;

    private Double moyenneGenerale;

    @Column(nullable = false)
    private Boolean valide = false;

    @JsonIgnore
    @OneToMany(mappedBy = "bulletin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MoyenneMatiere> moyennesMatieres;

    @JsonIgnore
    @OneToOne(mappedBy = "bulletin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Validation validation;

}
