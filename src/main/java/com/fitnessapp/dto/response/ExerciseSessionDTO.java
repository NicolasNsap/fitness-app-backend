package com.fitnessapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseSessionDTO {
    private LocalDate date;
    private Integer totalSet;
    private Double maxWeight;
    private Integer totalReps;
}
