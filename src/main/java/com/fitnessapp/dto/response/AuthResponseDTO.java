package com.fitnessapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Respuesta después de un login exitoso
 * es como un pase de entrada que te dan después de mostrar tu identificación en la puerta
 * - Token JWT (el "pase" para entrar a rutas protegidas)
 * - Tipo de token (siempre "Bearer")
 * - Información básica del usuario autenticado*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {

    /**
     * Token JWT generado
     * Ejemplo: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     * Este token debe incluirse en todas las peticiones posteriores:
     * Authorization: Bearer <token>
     */
    private String token;

    /**
     * Tipo de token
     * Siempre será "Bearer" (estándar OAuth 2.0)
     * El cliente debe enviar en el header:
     * Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
     */
    private String type = "Bearer";

    /**
     * Información básica del usuario autenticado
     * Incluimos:
     * - id: Para referencias
     * - username: Para mostrar en UI
     * - email: Para mostrar en perfil
     * - role: Para controlar permisos en el frontend
     */
    private UUID userId;
    private String username;
    private String email;
    private String roleName;

    /**
     * NOTA: El token JWT ya contiene esta información encriptada
     * Pero la incluimos aquí para que el frontend no tenga que
     * decodificar el JWT manualmente
     */
}
