package com.fitnessapp.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para actualizar un workout existente
 * Todos los campos son opcionales (solo se actualizan los que vienen)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWorkoutRequestDTO {

    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    private LocalDate date;

    private Integer durationMinutes;

    private String notes;

    private Boolean completed;

}
