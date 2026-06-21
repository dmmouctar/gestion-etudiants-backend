package com.school.gestion_etudiants.repository;

import com.school.gestion_etudiants.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    // Toutes les notes d'un étudiant
    List<Note> findByEtudiantId(Long etudiantId);

    // Toutes les notes d'un examen
    List<Note> findByExamenId(Long examenId);

    // Note précise d'un étudiant pour un examen
    Optional<Note> findByEtudiantIdAndExamenId(Long etudiantId, Long examenId);

    boolean existsByEtudiantIdAndExamenId(Long etudiantId, Long examenId);

    // Toutes les notes d'un étudiant pour une matière donnée
    @Query("SELECT n FROM Note n " +
            "JOIN n.examen ex " +
            "WHERE n.etudiant.id = :etudiantId " +
            "AND ex.matiere.id = :matiereId")
    List<Note> findByEtudiantIdAndMatiereId(
            @Param("etudiantId") Long etudiantId,
            @Param("matiereId") Long matiereId);

    // Moyenne d'un étudiant pour une matière
    @Query("SELECT AVG(n.valeur) FROM Note n " +
            "JOIN n.examen ex " +
            "WHERE n.etudiant.id = :etudiantId " +
            "AND ex.matiere.id = :matiereId")
    Optional<Double> calculerMoyenneParMatiere(
            @Param("etudiantId") Long etudiantId,
            @Param("matiereId") Long matiereId);
}