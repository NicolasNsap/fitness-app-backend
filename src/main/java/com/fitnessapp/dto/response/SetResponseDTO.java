package com.fitnessapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO de respuesta para una serie(set)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetResponseDTO {

    private UUID id;
    private Integer setNumber;
    private String setType;
    private Double weight;
    private Integer reps;
    private Integer rir;
    private Double rpe;
    private Integer restSeconds;
    private Boolean completed;
    private String notes;

    /**
     * Volumen del set (peso x reps)
     * Calculado automáticamente
     */
    private Double volume;
}
