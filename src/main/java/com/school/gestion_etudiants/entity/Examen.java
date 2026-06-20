package com.school.gestion_etudiants.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "examens")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Examen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_examen_id", nullable = false)
    private TypeExamen typeExamen;

    private LocalDate dateExamen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;

    @JsonIgnore
    @OneToMany(mappedBy = "examen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Note> notes;
}
