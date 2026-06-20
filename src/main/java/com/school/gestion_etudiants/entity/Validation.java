package com.school.gestion_etudiants.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "validations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Validation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulletin_id", nullable = false, unique = true)
    private Bulletin bulletin;

    @Column(nullable = false)
    private Boolean valide = false;

    private LocalDateTime validatedAt;
}
