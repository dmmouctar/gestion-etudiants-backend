package com.school.gestion_etudiants.service;

import com.school.gestion_etudiants.dto.request.ExamenRequest;
import com.school.gestion_etudiants.dto.response.ExamenResponse;
import com.school.gestion_etudiants.entity.Examen;
import com.school.gestion_etudiants.entity.Matiere;
import com.school.gestion_etudiants.entity.TypeExamen;
import com.school.gestion_etudiants.mapper.EntityMapper;
import com.school.gestion_etudiants.repository.ExamenRepository;
import com.school.gestion_etudiants.repository.MatiereRepository;
import com.school.gestion_etudiants.repository.TypeExamenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamenService {

    private final ExamenRepository examenRepository;
    private final MatiereRepository matiereRepository;
    private final TypeExamenRepository typeExamenRepository;
    private final EntityMapper mapper;

    @Transactional
    public ExamenResponse creer(ExamenRequest request) {
        Matiere matiere = matiereRepository.findById(request.getMatiereId())
                .orElseThrow(() -> new RuntimeException("Matière non trouvée"));

        TypeExamen typeExamen = typeExamenRepository.findById(request.getTypeExamenId())
                .orElseThrow(() -> new RuntimeException("Type d'examen non trouvé"));

        Examen examen = Examen.builder()
                .matiere(matiere)
                .typeExamen(typeExamen)
                .dateExamen(request.getDateExamen())
                .build();

        return mapper.toExamenResponse(examenRepository.save(examen));
    }

    @Transactional
    public ExamenResponse modifier(Long id, ExamenRequest request) {
        Examen examen = examenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Examen non trouvé : " + id));

        Matiere matiere = matiereRepository.findById(request.getMatiereId())
                .orElseThrow(() -> new RuntimeException("Matière non trouvée"));

        TypeExamen typeExamen = typeExamenRepository.findById(request.getTypeExamenId())
                .orElseThrow(() -> new RuntimeException("Type d'examen non trouvé"));

        examen.setMatiere(matiere);
        examen.setTypeExamen(typeExamen);
        examen.setDateExamen(request.getDateExamen());

        return mapper.toExamenResponse(examenRepository.save(examen));
    }

    @Transactional
    public void supprimer(Long id) {
        if (!examenRepository.existsById(id)) {
            throw new RuntimeException("Examen non trouvé : " + id);
        }
        examenRepository.deleteById(id);
    }

    @Transactional
    public ExamenResponse trouverParId(Long id) {
        return mapper.toExamenResponse(
                examenRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Examen non trouvé : " + id))
        );
    }

    @Transactional
    public List<ExamenResponse> listerTous() {
        return examenRepository.findAll()
                .stream()
                .map(mapper::toExamenResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ExamenResponse> listerParMatiere(Long matiereId) {
        return examenRepository.findByMatiereId(matiereId)
                .stream()
                .map(mapper::toExamenResponse)
                .collect(Collectors.toList());
    }
}