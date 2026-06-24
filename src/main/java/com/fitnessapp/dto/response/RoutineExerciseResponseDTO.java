package com.fitnessapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * RoutineExerciseResponseDTO - Info de un ejercicio dentro de la rutina
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineExerciseResponseDTO {

    private UUID id;
    private UUID exerciseId;
    private String exerciseName;
    private Integer sets;
    private Integer targetReps;
    private Integer restSeconds;
    private Integer orderIndex;
    private String notes;
}
