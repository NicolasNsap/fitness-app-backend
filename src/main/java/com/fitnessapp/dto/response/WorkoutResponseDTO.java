package com.fitnessapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO de respuesta para un workout completo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutResponseDTO {

    private UUID id;
    private String name;
    private LocalDate date;
    private Integer durationMinutes;
    private String notes;
    private Boolean completed;
    private LocalDateTime createdAt;

    /**
     * Información del usuario (solo ID y username)
     */
    private UUID userId;
    private String username;

    /**
     * Lista de ejercicios del workout
     */
    @Builder.Default
    private List<WorkoutExerciseResponseDTO> exercises = new ArrayList<>();

    /**
     * Estadísticas del workout
     */
    private Integer totalExercises;
    private Integer totalSets;
    private Double totalVolume;

}
