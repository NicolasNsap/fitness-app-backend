package com.fitnessapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * CreateRoutineExerciseRequestDTO - Datos que envía el usuario para agregar un ejercicio a una rutina
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoutineExerciseRequestDTO {

    @NotNull
    private UUID exerciseId;

    @NotNull
    private Integer orderIndex;

    @NotNull
    private Integer restSeconds;

    private String notes;

    @NotNull
    private Integer sets;

    @NotNull
    private Integer targetReps;

}
