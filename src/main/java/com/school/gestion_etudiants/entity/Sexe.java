package com.school.gestion_etudiants.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sexe")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Sexe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String nom;
}
