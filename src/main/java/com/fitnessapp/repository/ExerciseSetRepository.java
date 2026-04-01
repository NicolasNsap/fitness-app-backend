package com.fitnessapp.repository;

import com.fitnessapp.entity.ExerciseSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExerciseSetRepository extends JpaRepository<ExerciseSet, UUID> {

    /**
     * Busca todos los sets de un workout exercise ordenados
     * SQL: SELECT * FROM exercise_sets WHERE workout_exercise_id = ? ORDER BY set_number
     */
    List<ExerciseSet> findByWorkoutExerciseIdOrderBySetNumberAsc(UUID workoutExerciseId);

    /**
     * Busca el set con mayor peso para un ejercicio específico (PR - Personal Record)
     * Útil para mostrar récords personales
     */
    @Query("SELECT es FROM ExerciseSet es " +
            "JOIN es.workoutExercise we " +
            "WHERE we.exercise.id = :exerciseId " +
            "ORDER BY es.weight DESC " +
            "LIMIT 1")
    Optional<ExerciseSet> findTopByExerciseIdOrderByWeightDesc(@Param("exerciseId") UUID exerciseId);

    /**
     * Calcula el volumen total de un workout exercise
     * Volumen = suma de (peso * reps) de todos los sets
     */
    @Query("SELECT SUM(es.weight * es.reps) FROM ExerciseSet es " +
            "WHERE es.workoutExercise.id = :workoutExerciseId")
    Optional<Double> calculateTotalVolume(@Param("workoutExerciseId") UUID workoutExerciseId);

    /**
     * Cuenta sets completados de un workout exercise
     */
    long countByWorkoutExerciseIdAndCompletedTrue(UUID workoutExerciseId);

    /**
     * Elimina todos los sets de un workout exercise
     */
    void deleteByWorkoutExerciseId(UUID workoutExerciseId);
}
