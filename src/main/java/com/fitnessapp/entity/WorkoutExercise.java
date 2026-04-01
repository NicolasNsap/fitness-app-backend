package com.fitnessapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Ejercicio dentro de un workout(entrenaminto)
 * representa un ejercicio especifico dentro de una sesion de entrenamiento
 * tabla intermedia entre workout y exercise, pero con datos propios
 *
 * relaciones:
 * - Pertenece a un Workout (N:1)
 * - Referencia un Exercise del catálogo (N:1)
 * - Tiene muchos ExerciseSets (1:N)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "workout_exercises")
public class WorkoutExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * workout al que pertenece este ejercicio
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    /**
     * Ejercicio del catálogo que se está realizando
     * referencia a la tabla exercise(catálogo base)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    /**
     * Orden del ejercicio dentro del workout
     * permite ordenar la visualización y tracking
     */
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    /**
     * notas especificas para este ejercicio en este workout
     */
    @Column(columnDefinition = "TEXT")
    private String notes;

    /**
     * lista de series(sets) de este ejercicio
     *
     * CascadeType.ALL: Crear/actualizar/eliminar sets junto con el workout exercise
     * orphanRemoval: Eliminar sets huérfanos de la BD
     */
    @Builder.Default
    @OneToMany(mappedBy = "workoutExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("setNumber ASC")
    private List<ExerciseSet> sets = new ArrayList<>();

    // MÉTODOS HELPER

    /**
     *Agrega un set al ejercicio
     *Mantiene la relación bidireccional sincronizada
     */
    public void addSet(ExerciseSet set) {
        sets.add(set);
        set.setWorkoutExercise(this);
    }

    /**
     *Elimina un set del ejercicio
     */
    public void removeSet(ExerciseSet set) {
        sets.remove(set);
        set.setWorkoutExercise(null);
    }

    /**
     *Obtiene el número total de sets
     */
    public int getTotalSets() {
        return sets.size();
    }

    /**
     * Obtiene el número de sets completados
     */
    public long getCompletedSets() {
        return sets.stream()
                .filter(ExerciseSet::getCompleted)
                .count();
    }

    /**
     * Calcula el volumen total del ejercicio (suma de peso x reps de cada set)
     */
    public Double getTotalVolume() {
        return sets.stream()
                .mapToDouble(ExerciseSet::getVolume)
                .sum();
    }
}
