package com.fitnessapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ExerciseResponseDTO - Datos del ejercicio que ENVIAMOS al cliente
 * Incluye toda la información pública del ejercicio
 * Se usa para:
 * - Mostrar catálogo de ejercicios
 * - Detalles de un ejercicio específico
 * - Buscar ejercicios
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String muscleGroup;
    private String equipmentNeeded;
    private String difficultyLevel;
    private String mediaUrl;
    private Boolean isActive;
}
