package com.school.gestion_etudiants.repository;

import com.school.gestion_etudiants.entity.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ValidationRepository extends JpaRepository<Validation, Long> {

    Optional<Validation> findByBulletinId(Long bulletinId);

    boolean existsByBulletinId(Long bulletinId);
}