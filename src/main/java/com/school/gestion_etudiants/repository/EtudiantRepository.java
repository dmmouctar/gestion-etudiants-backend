package com.school.gestion_etudiants.repository;

import com.school.gestion_etudiants.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long>,
        JpaSpecificationExecutor<Etudiant> {

    // Trouver un étudiant par son compte user
    Optional<Etudiant> findByUserId(Long userId);

    // Vérifier si un CIN existe déjà
    boolean existsByCin(String cin);

    // Vérifier si un email existe déjà
    boolean existsByEmail(String email);

    // ← CORRECTION : méthode simplifiée qui évite le nom trop long
    @Query("SELECT e FROM Etudiant e WHERE " +
            "LOWER(e.nom) LIKE LOWER(CONCAT('%', :terme, '%')) OR " +
            "LOWER(e.prenom) LIKE LOWER(CONCAT('%', :terme, '%'))")
    List<Etudiant> rechercherParNomOuPrenom(@Param("terme") String terme);

    // Tous les étudiants d'une filière
    List<Etudiant> findByFiliereId(Long filiereId);

    // Tous les étudiants d'une année académique
    List<Etudiant> findByAnneeAcademiqueId(Long anneeAcademiqueId);

    // Tous les étudiants d'une filière ET d'une année académique
    List<Etudiant> findByFiliereIdAndAnneeAcademiqueId(Long filiereId, Long anneeAcademiqueId);

    // Compter les étudiants par filière
    long countByFiliereId(Long filiereId);

    // Chercher par CIN
    Optional<Etudiant> findByCin(String cin);

    // Tous les étudiants avec leurs relations chargées (évite le N+1)
    @Query("SELECT e FROM Etudiant e " +
            "LEFT JOIN FETCH e.filiere " +
            "LEFT JOIN FETCH e.anneeAcademique " +
            "LEFT JOIN FETCH e.sexe " +
            "WHERE e.id = :id")
    Optional<Etudiant> findByIdWithDetails(@Param("id") Long id);
}
