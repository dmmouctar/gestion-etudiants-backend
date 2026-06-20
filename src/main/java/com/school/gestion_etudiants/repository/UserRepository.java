package com.school.gestion_etudiants.repository;

import com.school.gestion_etudiants.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Trouver un user par email (pour l'authentification)
    Optional<User> findByEmail(String email);

    // Vérifier si un email existe déjà
    boolean existsByEmail(String email);


}
