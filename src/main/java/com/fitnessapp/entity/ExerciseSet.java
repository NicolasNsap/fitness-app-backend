package com.fitnessapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * serie de un ejercicio
 * representa una serie individual dentro de un ejercicio
 * contiene los datos reales peso, resps, etc.
 *
 * por conflictos con la clase Set de java se llaamara ExerciseSet
 * relaciones:
 * Pertenece a un WorkoutExercise (N:1)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "exercise_sets")
public class ExerciseSet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     *WorkoutExercise al que pertenece esta serie
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;

    /**
     * Número de la serie dentro del ejercicio
     * 1 = primera serie, 2 = segunda, etc.
     */
    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    /**
     * Tipo de serie
     * Ejemplos: "WARMUP", "WORKING", "DROP", "FAILURE"
     */
    @Builder.Default
    @Column(name = "set_type", length = 20)
    private String setType = "WORKING";

    /**
     * Peso utilizado en kilogramos
     * Usa Double para permitir decimales (ej: 22.5 kg)
     */
    @Column(nullable = false)
    private Double weight;

    /**
     * Número de repeticiones realizadas
     */
    @Column(nullable = false)
    private Integer reps;

    /**
     * RIR - Reps In Reserve (Repeticiones en reserva)
     * Cuántas reps más podrías haber hecho
     * 0 = fallo muscular, 1 = casi fallo, 2-3 = normal
     * Opcional: puede ser null si no se trackea
     */
    private Integer rir;

    /**
     * RPE - Rating of Perceived Exertion (Escala de esfuerzo percibido)
     * Escala del 1-10, donde 10 = máximo esfuerzo
     * Opcional: alternativa al RIR
     */
    private Double rpe;

    /**
     * Tiempo de descanso después de esta serie (en segundos)
     * Útil para trackear y planificar descansos
     */
    @Column(name = "rest_seconds")
    private Integer restSeconds;

    /**
     * Indica si la serie fue completada
     * Útil para workouts en progreso
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean completed = false;

    /**
     * Notas específicas de esta serie
     * Ejemplo: "Última rep con ayuda", "Forma perfecta"
     */
    @Column(length = 255)
    private String notes;


    // MÉTODOS HELPER

    /**
     * Calcula el volumen de la serie (peso x reps)
     * Útil para estadísticas
     */
    public Double getVolume() {
        return weight * reps;
    }

    /**
     * Verifica si es una serie de calentamiento
     */
    public boolean isWarmup() {
        return "WARMUP".equalsIgnoreCase(setType);
    }

    /**
     * Verifica si es una serie de trabajo
     */
    public boolean isWorkingSet() {
        return "WORKING".equalsIgnoreCase(setType);
    }
}
