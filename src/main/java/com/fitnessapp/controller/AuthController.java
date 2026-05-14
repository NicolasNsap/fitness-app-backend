package com.fitnessapp.controller;

import com.fitnessapp.dto.request.LoginRequestDTO;
import com.fitnessapp.dto.request.RegisterRequestDTO;
import com.fitnessapp.dto.response.AuthResponseDTO;
import com.fitnessapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  AUTH CONTROLLER - Endpoints de Autenticación
 *  Endpoints públicos (no requieren token):
 *  - POST /api/auth/register → Registrar nuevo usuario
 *  - POST /api/auth/login → Iniciar sesión
 *
 *  @author Nicolas Abarca
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    //constructor - Spring inyecta AuthService automáticamente
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    /**
     * Registra un nuevo usuario
     *
     * POST /api/auth/register
     *
     * Request Body:
     * {
     *   "username": "nombre",
     *   "email": "tu_email@email.com",
     *   "password": "tu password"
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        AuthResponseDTO response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Inicia sesión y retorna token JWT
     *
     * POST /api/auth/login
     *
     * Request Body:
     * {
     *   "identifier": "nicolas", puede ser username o email
     *   "password": "password"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}
