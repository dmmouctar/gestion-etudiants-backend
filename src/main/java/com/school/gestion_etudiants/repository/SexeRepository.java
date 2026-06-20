package com.school.gestion_etudiants.repository;

import com.school.gestion_etudiants.entity.Sexe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SexeRepository extends JpaRepository<Sexe, Long> {

    Optional<Sexe> findByNom(String nom);
}