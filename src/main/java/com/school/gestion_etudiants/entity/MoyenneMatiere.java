package com.school.gestion_etudiants.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "moyennes_matieres", uniqueConstraints = @UniqueConstraint(columnNames = {
        "bulletin_id", "matiere_id"
}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MoyenneMatiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulletin_id", nullable = false)
    private Bulletin bulletin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;

    private Double moyenne;
}
