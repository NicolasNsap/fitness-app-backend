package com.fitnessapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * UserResponseDTO - Datos del usuario que ENVIAMOS al cliente
 * Analogía: Es la "ficha pública" del usuario que mostramos
 * Solo información segura y relevante
 * CRÍTICO: NO incluye la contraseña
 * Se usa para:
 * - Mostrar perfil de usuario
 * - Listar usuarios (si eres ADMIN)
 * - Respuesta después de registro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    /*
    * Id del usuario
    */
    private UUID id;

    private String username;

    private String email;

    private String roleName;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /*
    no incluimos password para no exponerla
     */

}
