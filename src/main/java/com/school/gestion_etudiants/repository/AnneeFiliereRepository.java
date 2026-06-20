package com.school.gestion_etudiants.repository;

import com.school.gestion_etudiants.entity.AnneeFiliere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnneeFiliereRepository extends JpaRepository<AnneeFiliere, Long> {

    List<AnneeFiliere> findByFiliereId(Long filiereId);

    List<AnneeFiliere> findByAnneeAcademiqueId(Long anneeAcademiqueId);

    Optional<AnneeFiliere> findByFiliereIdAndAnneeAcademiqueId(
            Long filiereId, Long anneeAcademiqueId);

    boolean existsByFiliereIdAndAnneeAcademiqueId(
            Long filiereId, Long anneeAcademiqueId);

}
