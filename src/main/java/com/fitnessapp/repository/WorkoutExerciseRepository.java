package com.fitnessapp.repository;

import com.fitnessapp.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, UUID> {

    /**
     * Busca todos los ejercicios de un workout ordenados
     * SQL: SELECT * FROM workout_exercises WHERE workout_id = ? ORDER BY order_index
     */
    List<WorkoutExercise> findByWorkoutIdOrderByOrderIndexAsc(UUID workoutId);

    /**
     * Busca ejercicios por el ID del ejercicio base
     * Útil para ver historial de un ejercicio específico
     */
    List<WorkoutExercise> findByExerciseId(UUID exerciseId);

    /**
     * Cuenta cuántas veces se ha hecho un ejercicio
     */
    long countByExerciseId(UUID exerciseId);

    /**
     * Elimina todos los ejercicios de un workout
     */
    void deleteByWorkoutId(UUID workoutId);
}
