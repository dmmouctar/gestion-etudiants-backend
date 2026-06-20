package com.school.gestion_etudiants.controller;

import com.school.gestion_etudiants.dto.response.ApiResponse;
import com.school.gestion_etudiants.entity.User;
import com.school.gestion_etudiants.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final UserRepository userRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    // ── GET /api/profile/me ───────────────────────────────────────
    // Retourne les infos de l'utilisateur connecté + photoUrl
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMe(
            Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Map<String, Object> data = new HashMap<>();
        data.put("id",       user.getId());
        data.put("name",     user.getName());
        data.put("email",    user.getEmail());
        data.put("role",     user.getRole().name());
        data.put("photoUrl", user.getPhotoUrl());

        return ResponseEntity.ok(ApiResponse.success(data, "Profil chargé"));
    }

    // ── POST /api/profile/photo ───────────────────────────────────
    // Upload d'une photo de profil
    @PostMapping("/photo")
    @PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {

        // Vérification type de fichier
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Seules les images sont acceptées"));
        }

        // Créer le dossier si inexistant
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un nom unique pour le fichier
        String extension = getExtension(file.getOriginalFilename());
        String fileName  = UUID.randomUUID().toString() + "." + extension;
        Path filePath    = uploadPath.resolve(fileName);

        // Sauvegarder le fichier
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Mettre à jour la photo dans la base
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Supprimer l'ancienne photo si elle existe
        if (user.getPhotoUrl() != null) {
            try {
                String oldFileName = user.getPhotoUrl().replace("/api/profile/photos/", "");
                Files.deleteIfExists(uploadPath.resolve(oldFileName));
            } catch (Exception e) {
                log.warn("Impossible de supprimer l'ancienne photo : {}", e.getMessage());
            }
        }

        String photoUrl = "/api/profile/photos/" + fileName;
        user.setPhotoUrl(photoUrl);
        userRepository.save(user);

        Map<String, String> result = new HashMap<>();
        result.put("photoUrl", photoUrl);

        return ResponseEntity.ok(ApiResponse.success(result, "Photo mise à jour avec succès"));
    }

    // ── GET /api/profile/photos/{filename} ───────────────────────
    // Servir les photos stockées
    @GetMapping("/photos/{filename}")
    public ResponseEntity<Resource> servePhoto(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) contentType = "image/jpeg";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "jpg";
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    // ── POST /api/profile/photo/user/{userId} ─────────────────────
    // Upload photo pour un utilisateur spécifique (admin seulement)
    @PostMapping("/photo/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadPhotoForUser(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) throws IOException {

        // Vérification type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Seules les images sont acceptées"));
        }

        // Créer le dossier si inexistant
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Nom unique
        String extension = getExtension(file.getOriginalFilename());
        String fileName  = UUID.randomUUID().toString() + "." + extension;
        Path filePath    = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Trouver le user et mettre à jour
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + userId));

        // Supprimer ancienne photo
        if (user.getPhotoUrl() != null) {
            try {
                String oldFileName = user.getPhotoUrl().replace("/api/profile/photos/", "");
                Files.deleteIfExists(uploadPath.resolve(oldFileName));
            } catch (Exception e) {
                log.warn("Impossible de supprimer l'ancienne photo");
            }
        }

        String photoUrl = "/api/profile/photos/" + fileName;
        user.setPhotoUrl(photoUrl);
        userRepository.save(user);

        Map<String, String> result = new HashMap<>();
        result.put("photoUrl", photoUrl);

        return ResponseEntity.ok(ApiResponse.success(result, "Photo mise à jour"));
    }

}
