package com.fitnessapp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear una serie(set) de un ejercicio
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSetRequestDTO {

    @NotNull(message = "El numero de la serie es obligatorio")
    @Min(value = 1, message = "El numero de la serie debe ser almenos 1")
    private Integer setNumber;

    /**
     * tipo de serie: WARMUP, WORKING, DROP, FAILURE
     */
    @Builder.Default
    private String setType = "WORKING";

    @NotNull(message = "El peso es obligatorio")
    @Min(value = 0, message = "El peso no puede ser negativo")
    private Double weight;

    @NotNull(message = "Las repeticiones son obligatorias")
    @Min(value = 1, message = "Debe haber al menos 1 repetición")
    private Integer reps;

    /**
     * RIR - Reps In Reserve (opcional)
     */
    @Min(value = 0, message = "RIR no puede ser negativo")
    private Integer rir;

    /**
     * RPE - Rating of Perceived Exertion (opcional)
     */
    private Double rpe;

    /**
     * Descanso en segundos después del set (opcional)
     */
    private Integer restSeconds;

    /**
     * Notas del set (opcional)
     */
    private String notes;
}
