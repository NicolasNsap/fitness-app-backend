package com.fitnessapp.repository;

import com.fitnessapp.entity.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

    Optional<Exercise> findById(UUID id);
    /**
     * Busca un ejercicio por su nombre exacto
     * Genera: SELECT * FROM exercises WHERE name = ?
     */
    Optional<Exercise> findByName(String name);

    /**
     * Verifica si existe un ejercicio con ese nombre
     * Útil para evitar duplicados al crear ejercicios
     */
    boolean existsByName(String name);

    /**
     * Encuentra ejercicios por grupo muscular
     * Genera: SELECT * FROM exercises WHERE muscle_group = ?
     *
     * Ejemplo: findByMuscleGroup("PECHO") → todos los ejercicios de pecho
     */
    List<Exercise> findByMuscleGroup(String muscleGroup);

    /**
     * Encuentra ejercicios por nivel de dificultad
     * Genera: SELECT * FROM exercises WHERE difficulty_level = ?
     *
     * Ejemplo: findByDifficultyLevel("PRINCIPIANTE")
     */
    List<Exercise> findByDifficultyLevel(String difficultyLevel);

    /**
     * Encuentra ejercicios por equipamiento necesario
     * Genera: SELECT * FROM exercises WHERE equipment_needed = ?
     */
    List<Exercise> findByEquipmentNeeded(String equipmentNeeded);

    /**
     * Encuentra solo ejercicios activos
     * Útil para mostrar el catálogo público (sin ejercicios obsoletos)
     */
    List<Exercise> findByIsActiveTrue();

    /**
     * Encuentra ejercicios activos de un grupo muscular específico
     * Combina dos condiciones con AND
     * Genera: SELECT * FROM exercises
     *         WHERE muscle_group =? AND is_active = true
     */
    List<Exercise> findByMuscleGroupAndIsActiveTrue(String muscleGroup);


    /**
     * Encuentra ejercicios por grupo muscular y dificultad
     * Útil para recomendar ejercicios según nivel del usuario
     * Genera: SELECT * FROM exercises
     *         WHERE muscle_group = ? AND difficulty_level = ? AND is_active = true
     */
    List<Exercise> findByMuscleGroupAndDifficultyLevelAndIsActiveTrue(
            String muscleGroup,
            String difficultyLevel
    );

}
