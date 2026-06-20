package com.school.gestion_etudiants.repository;


import com.school.gestion_etudiants.entity.AnneeAcademique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnneeAcademiqueRepository extends JpaRepository<AnneeAcademique, Long> {

    Optional<AnneeAcademique> findByNom(String nom);

    boolean existsByNom(String nom);
}
