package com.school.gestion_etudiants.repository;

import com.school.gestion_etudiants.entity.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<Examen, Long> {

    // Tous les examens d'une matière
    List<Examen> findByMatiereId(Long matiereId);

    // Tous les examens d'un type (CC, Final, Rattrapage)
    List<Examen> findByTypeExamenId(Long typeExamenId);

    // Examens d'une matière par type
    List<Examen> findByMatiereIdAndTypeExamenId(Long matiereId, Long typeExamenId);

    // Examens d'une filière via la matière
    @Query("SELECT e FROM Examen e " +
            "JOIN e.matiere m " +
            "WHERE m.filiere.id = :filiereId")
    List<Examen> findByFiliereId(@Param("filiereId") Long filiereId);
}