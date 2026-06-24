package com.fitnessapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * RoutineResponseDTO - es para mostrar la rutina completa del usuario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineResponseDTO {

    private UUID id;
    private String name;
    private Integer durationMinutes;
    private String notes;

    @Builder.Default
    private List<RoutineExerciseResponseDTO> routineExercises =  new ArrayList<>();

    private LocalDateTime createdAt;

}
