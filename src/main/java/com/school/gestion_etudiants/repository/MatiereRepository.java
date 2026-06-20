package com.school.gestion_etudiants.repository;

import com.school.gestion_etudiants.entity.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {

    // Toutes les matières d'une filière
    List<Matiere> findByFiliereId(Long filiereId);

    // Recherche par nom
    List<Matiere> findByNomContainingIgnoreCase(String nom);

    boolean existsByNomAndFiliereId(String nom, Long filiereId);
}
