package com.fitnessapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO de respuesta para un ejercicio dentro de un workout
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseResponseDTO {

    private UUID id;
    private Integer orderIndex;
    private String notes;

    /**
     * Información del ejercicio base (del catálogo)
     */
    private UUID exerciseId;
    private String exerciseName;
    private String muscleGroup;

    /**
     * Lista de sets del ejercicio
     */
    @Builder.Default
    private List<SetResponseDTO> sets = new ArrayList<>();

    /**
     * Estadísticas calculadas
     */
    private Integer totalSets;
    private Integer completedSets;
    private Double totalVolume;

}
