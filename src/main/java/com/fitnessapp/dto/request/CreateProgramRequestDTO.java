package com.fitnessapp.dto.request;

import com.fitnessapp.entity.ProgressionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * CreateProgramRequestDTO - Creación del programa(meso ciclo)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProgramRequestDTO {

    @NotBlank(message = "El nombre del programa es obligatorio")
    private String name;

    @NotNull
    private Integer durationWeeks;

    @NotNull
    private LocalDate startDate;

    private String goal;

    private ProgressionType progressionType;

    @Builder.Default
    private List<CreateProgramRoutineRequestDTO> programRoutines = new ArrayList<>();

}
