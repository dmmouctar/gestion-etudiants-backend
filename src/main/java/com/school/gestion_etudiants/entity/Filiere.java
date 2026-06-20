package com.school.gestion_etudiants.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "filieres")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Filiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "filiere", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Matiere> matieres;

    @JsonIgnore
    @OneToMany(mappedBy = "filiere", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AnneeFiliere> anneeFilieres;
}
