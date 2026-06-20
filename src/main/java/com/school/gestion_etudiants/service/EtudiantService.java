package com.school.gestion_etudiants.service;

import com.school.gestion_etudiants.dto.request.EtudiantRequest;
import com.school.gestion_etudiants.dto.response.EtudiantResponse;
import com.school.gestion_etudiants.entity.*;
import com.school.gestion_etudiants.mapper.EntityMapper;
import com.school.gestion_etudiants.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final UserRepository userRepository;
    private final FiliereRepository filiereRepository;
    private final AnneeAcademiqueRepository anneeAcademiqueRepository;
    private final SexeRepository sexeRepository;
    private final EntityMapper mapper;
    private final PasswordEncoder passwordEncoder;

    // ── Créer un étudiant + son compte user ──────────────────────
    @Transactional
    public EtudiantResponse creer(EtudiantRequest request) {

        // Vérifications unicité
        if (userRepository.existsByEmail(request.getEmailCompte())) {
            throw new RuntimeException("Cet email de compte existe déjà : " + request.getEmailCompte());
        }
        if (request.getCin() != null && etudiantRepository.existsByCin(request.getCin())) {
            throw new RuntimeException("Ce CIN existe déjà : " + request.getCin());
        }

        // Récupérer les entités liées
        Filiere filiere = filiereRepository.findById(request.getFiliereId())
                .orElseThrow(() -> new RuntimeException("Filière non trouvée : " + request.getFiliereId()));

        AnneeAcademique annee = anneeAcademiqueRepository.findById(request.getAnneeAcademiqueId())
                .orElseThrow(() -> new RuntimeException("Année académique non trouvée : " + request.getAnneeAcademiqueId()));

        Sexe sexe = null;
        if (request.getSexeId() != null) {
            sexe = sexeRepository.findById(request.getSexeId())
                    .orElseThrow(() -> new RuntimeException("Sexe non trouvé : " + request.getSexeId()));
        }

        // Créer le compte User
        User user = User.builder()
                .name(request.getPrenom() + " " + request.getNom())
                .email(request.getEmailCompte())
                .password(passwordEncoder.encode(request.getMotDePasse()))
                .role(User.Role.ETUDIANT)
                .build();
        user = userRepository.save(user);

        // Créer l'étudiant
        Etudiant etudiant = Etudiant.builder()
                .user(user)
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .cin(request.getCin())
                .dateNaissance(request.getDateNaissance())
                .adresse(request.getAdresse())
                .telephone(request.getTelephone())
                .email(request.getEmail())
                .sexe(sexe)
                .filiere(filiere)
                .anneeAcademique(annee)
                .build();

        etudiant = etudiantRepository.save(etudiant);
        return mapper.toEtudiantResponse(etudiant);
    }

    // ── Modifier un étudiant ──────────────────────────────────────
    @Transactional
    public EtudiantResponse modifier(Long id, EtudiantRequest request) {

        Etudiant etudiant = etudiantRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé : " + id));

        // Vérif CIN unique (sauf si c'est le même étudiant)
        if (request.getCin() != null
                && !request.getCin().equals(etudiant.getCin())
                && etudiantRepository.existsByCin(request.getCin())) {
            throw new RuntimeException("Ce CIN existe déjà : " + request.getCin());
        }

        Filiere filiere = filiereRepository.findById(request.getFiliereId())
                .orElseThrow(() -> new RuntimeException("Filière non trouvée"));

        AnneeAcademique annee = anneeAcademiqueRepository.findById(request.getAnneeAcademiqueId())
                .orElseThrow(() -> new RuntimeException("Année académique non trouvée"));

        Sexe sexe = null;
        if (request.getSexeId() != null) {
            sexe = sexeRepository.findById(request.getSexeId())
                    .orElseThrow(() -> new RuntimeException("Sexe non trouvé"));
        }

        etudiant.setNom(request.getNom());
        etudiant.setPrenom(request.getPrenom());
        etudiant.setCin(request.getCin());
        etudiant.setDateNaissance(request.getDateNaissance());
        etudiant.setAdresse(request.getAdresse());
        etudiant.setTelephone(request.getTelephone());
        etudiant.setEmail(request.getEmail());
        etudiant.setSexe(sexe);
        etudiant.setFiliere(filiere);
        etudiant.setAnneeAcademique(annee);

        etudiant = etudiantRepository.save(etudiant);
        return mapper.toEtudiantResponse(etudiant);
    }

    // ── Supprimer un étudiant ─────────────────────────────────────
    @Transactional
    public void supprimer(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé : " + id));
        // Le cascade supprime aussi le User lié
        etudiantRepository.delete(etudiant);
    }

    // ── Trouver par ID ────────────────────────────────────────────
    @Transactional
    public EtudiantResponse trouverParId(Long id) {
        Etudiant etudiant = etudiantRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé : " + id));
        return mapper.toEtudiantResponse(etudiant);
    }

    // ── Trouver par User ID (pour l'étudiant connecté) ───────────
    @Transactional
    public EtudiantResponse trouverParUserId(Long userId) {
        Etudiant etudiant = etudiantRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé pour user : " + userId));
        return mapper.toEtudiantResponse(etudiant);
    }

    // ── Liste tous les étudiants ──────────────────────────────────
    @Transactional
    public List<EtudiantResponse> listerTous() {
        return etudiantRepository.findAll()
                .stream()
                .map(mapper::toEtudiantResponse)
                .collect(Collectors.toList());
    }

    // ── Recherche par nom ou prénom ───────────────────────────────
    @Transactional
    public List<EtudiantResponse> rechercher(String terme) {
        return etudiantRepository
                .rechercherParNomOuPrenom(terme)
                .stream()
                .map(mapper::toEtudiantResponse)
                .collect(Collectors.toList());
    }

    // ── Filtrer par filière ───────────────────────────────────────
    @Transactional
    public List<EtudiantResponse> listerParFiliere(Long filiereId) {
        return etudiantRepository.findByFiliereId(filiereId)
                .stream()
                .map(mapper::toEtudiantResponse)
                .collect(Collectors.toList());
    }

    // ── Filtrer par année académique ──────────────────────────────
    @Transactional
    public List<EtudiantResponse> listerParAnnee(Long anneeId) {
        return etudiantRepository.findByAnneeAcademiqueId(anneeId)
                .stream()
                .map(mapper::toEtudiantResponse)
                .collect(Collectors.toList());
    }

    // ── Filtrer par filière ET année ──────────────────────────────
    @Transactional
    public List<EtudiantResponse> listerParFiliereEtAnnee(Long filiereId, Long anneeId) {
        return etudiantRepository.findByFiliereIdAndAnneeAcademiqueId(filiereId, anneeId)
                .stream()
                .map(mapper::toEtudiantResponse)
                .collect(Collectors.toList());
    }
}