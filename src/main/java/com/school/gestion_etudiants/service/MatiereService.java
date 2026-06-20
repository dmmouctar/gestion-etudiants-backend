package com.school.gestion_etudiants.service;

import com.school.gestion_etudiants.dto.request.MatiereRequest;
import com.school.gestion_etudiants.dto.response.MatiereResponse;
import com.school.gestion_etudiants.entity.Filiere;
import com.school.gestion_etudiants.entity.Matiere;
import com.school.gestion_etudiants.mapper.EntityMapper;
import com.school.gestion_etudiants.repository.FiliereRepository;
import com.school.gestion_etudiants.repository.MatiereRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatiereService {

    private final MatiereRepository matiereRepository;
    private final FiliereRepository filiereRepository;
    private final EntityMapper mapper;

    @Transactional
    public MatiereResponse creer(MatiereRequest request) {
        if (matiereRepository.existsByNomAndFiliereId(request.getNom(), request.getFiliereId())) {
            throw new RuntimeException("Cette matière existe déjà dans cette filière");
        }
        Filiere filiere = filiereRepository.findById(request.getFiliereId())
                .orElseThrow(() -> new RuntimeException("Filière non trouvée"));

        Matiere matiere = Matiere.builder()
                .nom(request.getNom())
                .coefficient(request.getCoefficient())
                .filiere(filiere)
                .build();

        return mapper.toMatiereResponse(matiereRepository.save(matiere));
    }

    @Transactional
    public MatiereResponse modifier(Long id, MatiereRequest request) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée : " + id));

        Filiere filiere = filiereRepository.findById(request.getFiliereId())
                .orElseThrow(() -> new RuntimeException("Filière non trouvée"));

        matiere.setNom(request.getNom());
        matiere.setCoefficient(request.getCoefficient());
        matiere.setFiliere(filiere);

        return mapper.toMatiereResponse(matiereRepository.save(matiere));
    }

    @Transactional
    public void supprimer(Long id) {
        if (!matiereRepository.existsById(id)) {
            throw new RuntimeException("Matière non trouvée : " + id);
        }
        matiereRepository.deleteById(id);
    }

    @Transactional
    public MatiereResponse trouverParId(Long id) {
        return mapper.toMatiereResponse(
                matiereRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Matière non trouvée : " + id))
        );
    }

    @Transactional
    public List<MatiereResponse> listerTous() {
        return matiereRepository.findAll()
                .stream()
                .map(mapper::toMatiereResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MatiereResponse> listerParFiliere(Long filiereId) {
        return matiereRepository.findByFiliereId(filiereId)
                .stream()
                .map(mapper::toMatiereResponse)
                .collect(Collectors.toList());
    }
}