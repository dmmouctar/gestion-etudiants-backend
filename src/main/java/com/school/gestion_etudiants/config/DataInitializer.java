package com.school.gestion_etudiants.config;

import com.school.gestion_etudiants.entity.*;
import com.school.gestion_etudiants.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SexeRepository sexeRepository;
    private final TypeExamenRepository typeExamenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ── Créer le compte Admin si inexistant ───────────────────
        if (!userRepository.existsByEmail("admin@school.ma")) {
            User admin = User.builder()
                    .name("Administrateur")
                    .email("admin@school.ma")
                    .password(passwordEncoder.encode("Admin@1234"))
                    .role(User.Role.ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("✅ Compte admin créé : admin@school.ma / Admin@1234");
        }

        // ── Créer les sexes si inexistants ────────────────────────
        if (sexeRepository.count() == 0) {
            sexeRepository.save(Sexe.builder().nom("Masculin").build());
            sexeRepository.save(Sexe.builder().nom("Féminin").build());
            log.info("Sexes créés : Masculin, Féminin");
        }

        // ── Créer les types d'examens si inexistants ──────────────
        if (typeExamenRepository.count() == 0) {
            typeExamenRepository.save(TypeExamen.builder().nom("Contrôle Continu").build());
            typeExamenRepository.save(TypeExamen.builder().nom("Examen Final").build());
            typeExamenRepository.save(TypeExamen.builder().nom("Rattrapage").build());
            log.info("Types d'examens créés : CC, Final, Rattrapage");
        }
    }
}