package com.fitnessapp.mapper;

import com.fitnessapp.dto.request.UserRequestDTO;
import com.fitnessapp.dto.response.UserResponseDTO;
import com.fitnessapp.entity.User;
import org.springframework.stereotype.Component;

/**
 *clase utilitaria para transformar entre entity y dto.
 * ¿Por qué una clase separada?
 * - Evita repetir código de conversión en múltiples Services
 * - Centraliza las transformaciones en un solo lugar
 * - Facilita mantenimiento (si cambias un DTO, solo cambias aquí)
 * @Component: Spring la administra como bean inyectable
 * NOTA: Más adelante podrías usar librerías como MapStruct
 * que generan este código automáticamente
 */
@Component
public class UserMapper {

    /*
    * Convierte UserRequestDTO → User (Entity)
    * IMPORTANTE: Esta conversión es PARCIAL
     * No incluye:
     * - id (lo asigna la BD)
     * - role (lo asigna el Service)
     * - password encriptado (lo hace el Service)
     * - timestamps (los asigna JPA)
     * @param dto DTO con datos del request
     * @return Entity User sin guardar (aún falta configurar role y password)
     */

    public User toEntity(UserRequestDTO dto){
        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .passwordHash(dto.getPassword())
                .isActive(true)
                .build();
    }


    /*
    * Convierte User (Entity) → UserResponseDTO
    * esta conversion es completa y segura solo incluye datos que el cliente puede ver
    */
    public UserResponseDTO toResponseDTO(User user){
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roleName(user.getRole().getName())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }


}
