package com.fitnessapp.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO para agregar ejercicios a un workout
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkoutExerciseRequestDTO {

    @NotNull(message = "El ID del ejercicio es obligatorio")
    private UUID exerciseId;

    @NotNull(message = "El orden del ejercicio es obligatorio")
    @Min(value = 1, message = "El orden debe ser al menos 1")
    private Integer orderIndex;

    /**
     * Notas del ejercicio (opcional)
     */
    private String notes;

    /**
     * Lista de sets del ejercicio
     * Permite crear el ejercicio con sus sets en una sola petición
     */
    @Valid
    @Builder.Default
    private List<CreateSetRequestDTO> sets = new ArrayList<>();
}
