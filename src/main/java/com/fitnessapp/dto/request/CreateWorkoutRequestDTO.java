package com.fitnessapp.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para crear un nuevo workout
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkoutRequestDTO {

    @NotBlank(message = "El nombre del workout es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate date;

    /**
     * Duración en minutos (opcional, se puede agregar al finalizar)
     */
    private Integer durationMinutes;

    /**
     * Notas del workout (opcional)
     */
    private String notes;

    /**
     * Lista de ejercicios del workout
     * Permite crear el workout completo en una sola petición
     */
    @Valid
    @Builder.Default
    private List<CreateWorkoutExerciseRequestDTO> exercises = new ArrayList<>();
}
