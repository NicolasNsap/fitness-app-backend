package com.fitnessapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateRoutineRequestDTO - Datos necesarios para crear una rutina de ejercicios
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoutineRequestDTO {

    @NotBlank(message = "El nombre de la rutina es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    @NotNull
    private Integer durationMinutes;

    @NotEmpty
    @Builder.Default
    private List<CreateRoutineExerciseRequestDTO> routineExercises = new ArrayList<>();

    private String notes;


}
