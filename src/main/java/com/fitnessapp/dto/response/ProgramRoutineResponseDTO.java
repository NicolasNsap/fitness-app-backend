package com.fitnessapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * ProgramRoutineResponseDTO - Contendrá las rutinas del programa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramRoutineResponseDTO {

    private UUID id;
    private Integer dayNumber;

    private RoutineResponseDTO routine;
}
