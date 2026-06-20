package com.school.gestion_etudiants.controller;

import com.school.gestion_etudiants.dto.request.LoginRequest;
import com.school.gestion_etudiants.dto.response.ApiResponse;
import com.school.gestion_etudiants.dto.response.AuthResponse;
import com.school.gestion_etudiants.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Connexion réussie")
        );
    }
}
