package com.school.gestion_etudiants.repository;

import com.school.gestion_etudiants.entity.MoyenneMatiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MoyenneMatiereRepository extends JpaRepository<MoyenneMatiere, Long> {

    List<MoyenneMatiere> findByBulletinId(Long bulletinId);

    Optional<MoyenneMatiere> findByBulletinIdAndMatiereId(Long bulletinId, Long matiereId);

    boolean existsByBulletinIdAndMatiereId(Long bulletinId, Long matiereId);
}