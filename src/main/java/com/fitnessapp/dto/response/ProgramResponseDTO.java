package com.fitnessapp.dto.response;

import com.fitnessapp.entity.ProgressionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ProgramResponseDTO - Lo que verá el usuario de su programa(mesociclo)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramResponseDTO {

    private UUID id;
    private String name;
    private LocalDate startDate;
    private Integer durationWeeks;
    private String goal;
    private ProgressionType progressionType;
    private Boolean active;

    private List<ProgramRoutineResponseDTO> programRoutines;

}
