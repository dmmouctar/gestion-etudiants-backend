package com.school.gestion_etudiants.service;

import com.school.gestion_etudiants.dto.request.NoteRequest;
import com.school.gestion_etudiants.dto.response.NoteResponse;
import com.school.gestion_etudiants.entity.Etudiant;
import com.school.gestion_etudiants.entity.Examen;
import com.school.gestion_etudiants.entity.Note;
import com.school.gestion_etudiants.mapper.EntityMapper;
import com.school.gestion_etudiants.repository.EtudiantRepository;
import com.school.gestion_etudiants.repository.ExamenRepository;
import com.school.gestion_etudiants.repository.NoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final EtudiantRepository etudiantRepository;
    private final ExamenRepository examenRepository;
    private final EntityMapper mapper;

    @Transactional
    public NoteResponse saisir(NoteRequest request) {
        // Si la note existe déjà, on la met à jour
        Note note = noteRepository
                .findByEtudiantIdAndExamenId(request.getEtudiantId(), request.getExamenId())
                .orElse(null);

        if (note == null) {
            Etudiant etudiant = etudiantRepository.findById(request.getEtudiantId())
                    .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

            Examen examen = examenRepository.findById(request.getExamenId())
                    .orElseThrow(() -> new RuntimeException("Examen non trouvé"));

            note = Note.builder()
                    .etudiant(etudiant)
                    .examen(examen)
                    .valeur(request.getValeur())
                    .build();
        } else {
            // Mise à jour de la note existante
            note.setValeur(request.getValeur());
        }

        return mapper.toNoteResponse(noteRepository.save(note));
    }

    @Transactional
    public void supprimer(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new RuntimeException("Note non trouvée : " + id);
        }
        noteRepository.deleteById(id);
    }

    @Transactional
    public List<NoteResponse> listerParEtudiant(Long etudiantId) {
        return noteRepository.findByEtudiantId(etudiantId)
                .stream()
                .map(mapper::toNoteResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<NoteResponse> listerParExamen(Long examenId) {
        return noteRepository.findByExamenId(examenId)
                .stream()
                .map(mapper::toNoteResponse)
                .collect(Collectors.toList());
    }
}