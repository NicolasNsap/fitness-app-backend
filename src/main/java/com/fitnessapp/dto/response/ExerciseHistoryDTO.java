package com.fitnessapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseHistoryDTO {
    private String exerciseName;
    private UUID exerciseId;
    private LocalDate lastPerformed;
    private List<ExerciseSessionDTO> history;
}
