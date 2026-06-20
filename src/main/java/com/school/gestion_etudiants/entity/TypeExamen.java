package com.school.gestion_etudiants.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "types_examens")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TypeExamen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nom;

    @JsonIgnore
    @OneToMany(mappedBy = "typeExamen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Examen> examens;
}
