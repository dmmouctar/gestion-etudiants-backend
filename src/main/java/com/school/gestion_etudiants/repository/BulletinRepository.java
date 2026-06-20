package com.school.gestion_etudiants.repository;

import com.school.gestion_etudiants.entity.Bulletin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BulletinRepository extends JpaRepository<Bulletin, Long> {

    // Bulletin d'un étudiant pour une année académique
    Optional<Bulletin> findByEtudiantIdAndAnneeAcademiqueId(
            Long etudiantId, Long anneeAcademiqueId);

    // Tous les bulletins d'un étudiant
    List<Bulletin> findByEtudiantId(Long etudiantId);

    // Tous les bulletins d'une année académique
    List<Bulletin> findByAnneeAcademiqueId(Long anneeAcademiqueId);

    // Bulletins validés d'une année
    List<Bulletin> findByAnneeAcademiqueIdAndValide(Long anneeAcademiqueId, Boolean valide);

    boolean existsByEtudiantIdAndAnneeAcademiqueId(Long etudiantId, Long anneeAcademiqueId);

    // Bulletin avec toutes ses relations chargées
    @Query("SELECT b FROM Bulletin b " +
            "LEFT JOIN FETCH b.moyennesMatieres mm " +
            "LEFT JOIN FETCH mm.matiere " +
            "LEFT JOIN FETCH b.validation " +
            "WHERE b.etudiant.id = :etudiantId " +
            "AND b.anneeAcademique.id = :anneeId")
    Optional<Bulletin> findWithDetailsByEtudiantAndAnnee(
            @Param("etudiantId") Long etudiantId,
            @Param("anneeId") Long anneeId);
}