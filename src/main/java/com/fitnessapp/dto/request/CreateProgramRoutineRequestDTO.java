package com.fitnessapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * CreateProgramRoutineRequestDTO - lista donde irán las rutinas del programa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProgramRoutineRequestDTO {

    @NotNull
    private UUID routineId;
    @NotNull
    private Integer dayNumber;
}
