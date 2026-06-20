package com.school.gestion_etudiants.service;

import com.school.gestion_etudiants.dto.request.LoginRequest;
import com.school.gestion_etudiants.dto.response.AuthResponse;
import com.school.gestion_etudiants.entity.Etudiant;
import com.school.gestion_etudiants.entity.User;
import com.school.gestion_etudiants.repository.EtudiantRepository;
import com.school.gestion_etudiants.repository.UserRepository;
import com.school.gestion_etudiants.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EtudiantRepository etudiantRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        // Spring Security vérifie email + password
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String token = jwtService.generateToken(user);

        // Si c'est un étudiant, on récupère son ID étudiant
        Long etudiantId = null;
        if (user.getRole() == User.Role.ETUDIANT) {
            etudiantId = etudiantRepository.findByUserId(user.getId())
                    .map(Etudiant::getId)
                    .orElse(null);
        }

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .etudiantId(etudiantId)
                .build();
    }
}