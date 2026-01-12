package com.fitnessapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ExerciseRequestDTO - Datos necesarios para CREAR o ACTUALIZAR un ejercicio
 * Solo el ADMIN pueden crear/editar ejercicios
 * Los USER solo pueden consultarlos y usarlos en sus rutinas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseRequestDTO {

    @NotBlank(message = "El nombre del ejercicio es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    @NotBlank(message = "El grupo muscular es obligatorio")
    @Size(max = 50, message = "El grupo muscular no puede exceder 50 caracteres")
    private String muscleGroup;

    @Size(max = 100, message = "El equipamiento no puede exceder 100 caracteres")
    private String equipmentNeeded;

    @Size(max = 20, message = "El nivel de dificultad no puede exceder 20 caracteres")
    private String difficultyLevel;

    @Size(max = 255, message = "La URL del medio no puede exceder 255 caracteres")
    private String videoUrl;
}
