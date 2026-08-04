package com.fitnessapp.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSetRequestDTO {

    @Min(value = 0, message = "El peso no puede ser negativo")
    private Double weight;

    @Min(value = 1, message = "Debe haber al menos 1 repetición")
    private Integer reps;
}
